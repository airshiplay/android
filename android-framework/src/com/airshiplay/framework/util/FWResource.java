/**
 * 
 */
package com.airshiplay.framework.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.airshiplay.framework.application.FWApplication;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class FWResource {
	private static FWResource fwResource;
	private String pkg;
	private Resources resources;

	private FWResource(Context context) {
		this.pkg = context.getPackageName();
		this.resources = context.getResources();
	}

	public static FWResource getInstance(Context context) {
		FWApplication application;
		if (fwResource == null) {
			application = FWApplication.getInstance();
			Context localContext;
			if (application != null)
				localContext = application.getApplicationContext();
			else
				localContext = context.getApplicationContext();
			fwResource = new FWResource(localContext);
		}
		return fwResource;
	}

	public int getAnimId(String name) {
		try {
			return this.resources.getIdentifier(name, "anim", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	public int getArrayId(String name) {
		try {
			return this.resources.getIdentifier(name, "array", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	public int getColor(String name) {
		try {
			return this.resources.getColor(getColorId(name));
		} catch (Exception e) {
		}
		return 0;
	}

	public int getColorId(String name) {
		try {
			return this.resources.getIdentifier(name, "color", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	public int getDimenId(String name) {
		try {
			return this.resources.getIdentifier(name, "dimen", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	public Drawable getDrawable(String name) {
		try {
			return this.resources.getDrawable(getDrawableId(name));
		} catch (Exception e) {
		}
		return null;
	}

	public int getDrawableId(String name) {
		try {
			return this.resources.getIdentifier(name, "drawable", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	public int getLayoutId(String name) {
		try {
			return this.resources.getIdentifier(name, "layout", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	public int getRawId(String name) {
		try {
			return this.resources.getIdentifier(name, "raw", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * @param context
	 * @param defType
	 *            Optional default resource type to find, if "type/" is not
	 *            included in the name. Can be null to require an explicit type.
	 * @param name
	 *            The name of the desired resource.
	 * @return int The associated resource identifier. Returns 0 if no such
	 *         resource was found. (0 is not a valid resource ID.)
	 */
	protected int getResourcesId(Context context, String defType, String name) {
		try {
			return this.resources.getIdentifier(name, defType, this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	public String getString(String name) {
		int identifier = getStringId(name);
		try {
			return this.resources.getString(identifier);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Return a resource identifier for the given resource string name.
	 * 
	 * @param name
	 *            The name of the desired resource
	 * @return int The associated resource identifier. Returns 0 if no such
	 *         resource was found. (0 is not a valid resource ID.)
	 */
	public int getStringId(String name) {
		try {
			return this.resources.getIdentifier(name, "string", this.pkg);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * Return a resource identifier for the given resource style name.
	 * 
	 * @param name
	 *            The name of the desired resource
	 * @return int The associated resource identifier. Returns 0 if no such
	 *         resource was found. (0 is not a valid resource ID.)
	 */
	public int getStyleId(String name) {
		try {
			int i = this.resources.getIdentifier(name, "style", this.pkg);
			return i;
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * Return a resource identifier for the given resource view name.
	 * 
	 * @param name
	 *            The name of the desired resource
	 * @return int The associated resource identifier. Returns 0 if no such
	 *         resource was found. (0 is not a valid resource ID.)
	 */
	public int getViewId(String name) {
		try {
			int i = this.resources.getIdentifier(name, "id", this.pkg);
			return i;
		} catch (Exception e) {
		}
		return 0;
	}
}
