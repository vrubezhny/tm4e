/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package fr.opensagres.language.textmate.core.model;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.language.textmate.core.grammar.IGrammar;

/**
 * Abstract class for TextMate model.
 *
 */
public abstract class AbstractTMModel2 implements ITMModel {

	/**
	 * The TextMate grammar to use to parse for each lines of the document the
	 * TextMate tokens.
	 **/
	private IGrammar grammar;

	/** Listener when TextMate model tokens changed **/
	private final List<IModelTokensChangedListener> listeners;

	/** The background thread. */
	private BackgroundThread fThread;
	/** The background thread delay. */
	private int fDelay = 500;
	/** Queue to manage the changes applied to the text viewer. */
	private IModelLines fDirtyRegionQueue;

	private final IModelLines lines;

	private boolean initialized;

	public AbstractTMModel2() {
		this.listeners = new ArrayList<>();
		this.lines = new LineList();
		fDirtyRegionQueue = lines;
		fThread = new BackgroundThread(getClass().getName());
	}

	class LineList extends ArrayList<ModelLine> implements IModelLines {

		@Override
		public void addLine(int line) {
			try {
				add(new ModelLine(getLineText(line)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void removeLine(int line) {
			remove(line);
		}

		@Override
		public boolean add(ModelLine line) {
			fThread.reset();
			return super.add(line);
		}

		@Override
		public ModelLine remove(int index) {
			fThread.reset();
			return super.remove(index);
		}

		@Override
		public void updateLine(int line) {
			try {
				super.get(line).text = getLineText(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getSize() {
			return size();
		}
	}

	protected class DirtyRegionQueue {

		public int getSize() {
			return 0;
		}

	}

	protected class DirtyLine {

	}

	class BackgroundThread extends Thread {

		/** Has the reconciler been canceled. */
		private boolean fCanceled = false;
		/** Has the reconciler been reset. */
		private boolean fReset = false;
		/** Some changes need to be processed. */
		private boolean fIsDirty = false;
		/** Is a reconciling strategy active. */
		private boolean fIsActive = false;

		/**
		 * Creates a new background thread. The thread runs with minimal
		 * priority.
		 *
		 * @param name
		 *            the thread's name
		 */
		public BackgroundThread(String name) {
			super(name);
			setPriority(Thread.MIN_PRIORITY);
			setDaemon(true);
		}

		/**
		 * Suspends the caller of this method until this background thread has
		 * emptied the dirty region queue.
		 */
		public void suspendCallerWhileDirty() {
			boolean isDirty;
			do {
				synchronized (fDirtyRegionQueue) {
					isDirty = fDirtyRegionQueue.getSize() > 0;
					if (isDirty) {
						try {
							fDirtyRegionQueue.wait();
						} catch (InterruptedException x) {
						}
					}
				}
			} while (isDirty);
		}

		/**
		 * Reset the background thread as the text viewer has been changed,
		 */
		public void reset() {

			if (fDelay > 0) {

				synchronized (this) {
					fIsDirty = true;
					fReset = true;
				}

			} else {

				synchronized (this) {
					fIsDirty = true;
				}

				synchronized (fDirtyRegionQueue) {
					fDirtyRegionQueue.notifyAll();
				}
			}

			reconcilerReset();
		}

		@Override
		public void run() {
			synchronized (fDirtyRegionQueue) {
				try {
					fDirtyRegionQueue.wait(fDelay);
				} catch (InterruptedException x) {
				}
			}
		}

		/**
		 * Returns whether a reconciling strategy is active right now.
		 *
		 * @return <code>true</code> if a activity is active
		 */
		public boolean isActive() {
			return fIsActive;
		}

		/**
		 * Returns whether some changes need to be processed.
		 *
		 * @return <code>true</code> if changes wait to be processed
		 * @since 3.0
		 */
		public synchronized boolean isDirty() {
			return fIsDirty;
		}

		/**
		 * Cancels the background thread.
		 */
		public void cancel() {
			fCanceled = true;
			// IProgressMonitor pm= fProgressMonitor;
			// if (pm != null)
			// pm.setCanceled(true);
			// synchronized (fDirtyRegionQueue) {
			// fDirtyRegionQueue.notifyAll();
			// }
		}
	}

	@Override
	public IGrammar getGrammar() {
		return grammar;
	}

	@Override
	public void setGrammar(IGrammar grammar) {
		this.grammar = grammar;
	}

	@Override
	public void addModelTokensChangedListener(IModelTokensChangedListener listener) {
		initializeIfNeeded();
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	@Override
	public void removeModelTokensChangedListener(IModelTokensChangedListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public void dispose() {
		synchronized (this) {
			BackgroundThread bt = fThread;
			fThread = null;
			bt.cancel();
		}
	}

	private void initializeIfNeeded() {
		if (!initialized) {
			initialize();
			initialized = true;
		}
	}

	protected void initialize() {
		int linesLength = getNumberOfLines();
		synchronized (lines) {
			for (int line = 0; line < linesLength; line++) {
				try {
					lines.addLine(line);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		_resetTokenizationState();
	}

	private void _resetTokenizationState() {
		// TODO Auto-generated method stub

	}

	/**
	 * Starts the reconciler to reconcile the queued dirty-regions. Clients may
	 * extend this method.
	 */
	protected synchronized void startReconciling() {
		if (fThread == null)
			return;

		if (!fThread.isAlive()) {
			try {
				fThread.start();
			} catch (IllegalThreadStateException e) {
				// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=40549
				// This is the only instance where the thread is started; since
				// we checked that it is not alive, it must be dead already due
				// to a run-time exception or error. Exit.
			}
		} else {
			fThread.reset();
		}
	}

	/**
	 * Hook that is called after the reconciler thread has been reset.
	 */
	protected void reconcilerReset() {
	}

	/**
	 * Tells whether the code is running in this reconciler's background thread.
	 *
	 * @return <code>true</code> if running in this reconciler's background
	 *         thread
	 * @since 3.4
	 */
	protected boolean isRunningInReconcilerThread() {
		return Thread.currentThread() == fThread;
	}

	// protected void updateLines(DirtyLines lines) {
	//
	// if (fThread.isActive() || !fThread.isDirty() && fThread.isAlive()) {
	// // if (!fIsAllowedToModifyDocument && Thread.currentThread() ==
	// // fThread)
	// // throw new UnsupportedOperationException("The reconciler thread is
	// // not allowed to modify the document"); //$NON-NLS-1$
	// aboutToBeReconciled();
	// }
	//
	// /*
	// * The second OR condition handles the case when the document gets
	// * changed while still inside initialProcess().
	// */
	// // if (fThread.isActive() || fThread.isDirty() && fThread.isAlive())
	// // fProgressMonitor.setCanceled(true);
	//
	// // if (fIsIncrementalReconciler)
	// createDirtyLines(lines);
	//
	// fThread.reset();
	//
	// }

	protected void aboutToBeReconciled() {
		// TODO Auto-generated method stub

	}

	public IModelLines getLines() {
		return lines;
	}

	protected void _invalidateLine(int startLine) {
		System.err.println("START--------------------------");
		for (ModelLine line : ((List<ModelLine>) getLines())) {
			System.err.println(line.text);
		}
		System.err.println("END--------------------------");
	}

	protected abstract int getNumberOfLines();

	protected abstract String getLineText(int line) throws Exception;
}
