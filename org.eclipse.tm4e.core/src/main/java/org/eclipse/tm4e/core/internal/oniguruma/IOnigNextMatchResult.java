/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * This code is an translation of code copyrighted by https://github.com/atom/node-oniguruma, and initially licensed under Copyright (c) 2013 GitHub Inc..
 *
 * Contributors:
 *  - https://github.com/atom/node-oniguruma
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.core.internal.oniguruma;

public interface IOnigNextMatchResult {

	int getIndex();

	IOnigCaptureIndex[] getCaptureIndices();
}
