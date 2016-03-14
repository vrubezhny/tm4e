package fr.opensagres.language.textmate.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegExpSourceList {

	private class IRegExpSourceListAnchorCache {

		public ICompiledRule A0_G0;
		public ICompiledRule A0_G1;
		public ICompiledRule A1_G0;
		public ICompiledRule A1_G1;

		public IRegExpSourceListAnchorCache(ICompiledRule A0_G0, ICompiledRule A0_G1, ICompiledRule A1_G0,
				ICompiledRule A1_G1) {
			this.A0_G0 = A0_G0;
			this.A0_G1 = A0_G1;
			this.A1_G0 = A1_G0;
			this.A1_G1 = A1_G1;
		}

	}

	private List<RegExpSource> _items;
	private boolean _hasAnchors;
	private ICompiledRule _cached;
	private IRegExpSourceListAnchorCache _anchorCache;
	// private _cachedSources: string[];

	public RegExpSourceList() {
		this._items = new ArrayList<RegExpSource>();
		this._hasAnchors = false;
		this._cached = null;
		// this._cachedSources = null;
		this._anchorCache = new IRegExpSourceListAnchorCache(null, null, null, null);
	}

	public void push(RegExpSource item) {
		this._items.add(item);
		this._hasAnchors = this._hasAnchors ? this._hasAnchors : item.hasAnchor;
	}

	public void unshift(RegExpSource item) {
		this._items.remove(item);
		this._hasAnchors = this._hasAnchors ? this._hasAnchors : item.hasAnchor;
	}

	public int length() {
		return this._items.size();
	}

	public void setSource(int index, String newSource) {
		RegExpSource r = this._items.get(index);
		if (!r.source.equals(newSource)) {
			// bust the cache
			this._cached = null;
			this._anchorCache.A0_G0 = null;
			this._anchorCache.A0_G1 = null;
			this._anchorCache.A1_G0 = null;
			this._anchorCache.A1_G1 = null;
			r.setSource(newSource);
		}
	}

	public ICompiledRule compile(IRuleRegistry grammar, boolean allowA, boolean allowG) {
		if (!this._hasAnchors) {
			if (this._cached == null) {
				// this._cached = {
				// scanner: createOnigScanner(this._items.map(e => e.source)),
				// rules: this._items.map(e => e.ruleId)
				// };
				this._cached = new ICompiledRule(getScanner(), getRules());
			}
			return this._cached;
		} else {
			this._anchorCache = new IRegExpSourceListAnchorCache(
					(this._anchorCache.A0_G0 != null || (allowA == false && allowG == false)
							? this._resolveAnchors(allowA, allowG) : null),
					(this._anchorCache.A0_G1 != null || (allowA == false && allowG == true)
							? this._resolveAnchors(allowA, allowG) : null),
					(this._anchorCache.A1_G0 != null || (allowA == true && allowG == false)
							? this._resolveAnchors(allowA, allowG) : null),
					(this._anchorCache.A1_G1 != null || (allowA == true && allowG == true)
							? this._resolveAnchors(allowA, allowG) : null));
			if (allowA) {
				if (allowG) {
					return this._anchorCache.A1_G1;
				} else {
					return this._anchorCache.A1_G0;
				}
			} else {
				if (allowG) {
					return this._anchorCache.A0_G1;
				} else {
					return this._anchorCache.A0_G0;
				}
			}
		}

	}

	private ICompiledRule _resolveAnchors(boolean allowA, boolean allowG) {
		// return {
		// scanner: createOnigScanner(this._items.map(e =>
		// e.resolveAnchors(allowA, allowG))),
		// rules: this._items.map(e => e.ruleId)
		// };
		return new ICompiledRule(getScanner(), getRules());
	}

	private Object getScanner() {
		return null;
	}

	private Integer[] getRules() {
		Collection<Integer> ruleIds = new ArrayList<Integer>();
		for (RegExpSource item : this._items) {
			ruleIds.add(item.ruleId);
		}
		return ruleIds.toArray(new Integer[0]);
	}

}
