package fr.opensagres.language.textmate.rule;

import java.util.ArrayList;
import java.util.List;

public class RegExpSourceList {

	private class IRegExpSourceListAnchorCache {

		public Object A0_G0;
		public Object A0_G1;
		public Object A1_G0;
		public Object A1_G1;
		
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
		//this._cachedSources = null;
		this._anchorCache = new IRegExpSourceListAnchorCache();
	}

	public void push(RegExpSource item) {
		this._items.add(item);
		this._hasAnchors = this._hasAnchors ? this._hasAnchors : item.hasAnchor;
	}

	public void unshift(RegExpSource item) {
		this._items.remove(item);
		this._hasAnchors = this._hasAnchors ? this._hasAnchors : item.hasAnchor;
	}

	public int length(){
		return this._items.size();
	}

	public void setSource(int index, String newSource) {
		RegExpSource r = this._items.get(index);
		if (!r.getSource().equals(newSource)) {
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
			if (!this._cached) {
				this._cached = {
					scanner: createOnigScanner(this._items.map(e => e.source)),
					rules: this._items.map(e => e.ruleId)
				};
			}
			return this._cached;
		} else {
			this._anchorCache = {
				A0_G0: this._anchorCache.A0_G0 || (allowA === false && allowG === false ? this._resolveAnchors(allowA, allowG) : null),
				A0_G1: this._anchorCache.A0_G1 || (allowA === false && allowG === true ? this._resolveAnchors(allowA, allowG) : null),
				A1_G0: this._anchorCache.A1_G0 || (allowA === true && allowG === false ? this._resolveAnchors(allowA, allowG) : null),
				A1_G1: this._anchorCache.A1_G1 || (allowA === true && allowG === true ? this._resolveAnchors(allowA, allowG) : null),
			};
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
		return {
			scanner: createOnigScanner(this._items.map(e => e.resolveAnchors(allowA, allowG))),
			rules: this._items.map(e => e.ruleId)
		};
	}

}
