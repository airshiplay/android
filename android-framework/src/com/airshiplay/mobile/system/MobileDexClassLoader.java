package com.airshiplay.mobile.system;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import com.airshiplay.mobile.log.Logger;
import com.airshiplay.mobile.log.LoggerFactory;

import dalvik.system.DexClassLoader;

public class MobileDexClassLoader extends DexClassLoader {
	private static Logger log = LoggerFactory.getLogger(MobileDexClassLoader.class.getName());
	private DexClassLoader oldClassLoader;

	public MobileDexClassLoader(String dexPath, String optimizedDirectory, ClassLoader parent) {
		this(dexPath, optimizedDirectory, null, parent);
	}

	public MobileDexClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
		super(dexPath, optimizedDirectory, libraryPath, parent);
		if (parent instanceof DexClassLoader)
			oldClassLoader = (DexClassLoader) parent;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		log.debug("findClass=%s", name);
		return super.findClass(name);
	}

	@Override
	public String findLibrary(String name) {
		log.debug("findLibrary=%s", name);
		return super.findLibrary(name);
	}

	@Override
	protected URL findResource(String name) {
		log.debug("findResource=%s", name);
		return super.findResource(name);
	}

	@Override
	protected Enumeration<URL> findResources(String name) {
		log.debug("findResources=%s", name);
		return super.findResources(name);
	}

	@Override
	protected synchronized Package getPackage(String name) {
		log.debug("getPackage=%s", name);
		return super.getPackage(name);
	}

	@Override
	public String toString() {
		log.debug("toString");
		return super.toString();
	}

	@Override
	public URL getResource(String resName) {
		log.debug("getResource=%s", resName);
		return super.getResource(resName);
	}

	@Override
	public Enumeration<URL> getResources(String resName) throws IOException {
		log.debug("getResources=%s", resName);
		return super.getResources(resName);
	}

	@Override
	public InputStream getResourceAsStream(String resName) {
		log.debug("getResourceAsStream=%s", resName);
		return super.getResourceAsStream(resName);
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		log.debug("loadClass=%s", className);
		return super.loadClass(className);
	}

	@Override
	protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
		log.debug("loadClass=%s,%s", className, resolve);
		return super.loadClass(className, resolve);
	}

	@Override
	protected Package[] getPackages() {
		log.debug("getPackages");
		return super.getPackages();
	}

	@Override
	protected Package definePackage(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion,
			String implVendor, URL sealBase) throws IllegalArgumentException {
		return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
	}

	@Override
	public void setClassAssertionStatus(String cname, boolean enable) {
		super.setClassAssertionStatus(cname, enable);
	}

	@Override
	public void setPackageAssertionStatus(String pname, boolean enable) {
		super.setPackageAssertionStatus(pname, enable);
	}

	@Override
	public void setDefaultAssertionStatus(boolean enable) {
		super.setDefaultAssertionStatus(enable);
	}

	@Override
	public void clearAssertionStatus() {
		super.clearAssertionStatus();
	}

}
