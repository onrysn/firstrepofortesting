package com.mobit;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import mobit.eemr.Lun_Control;

public abstract class ICamera {

	private int min = 0, max = -1;
	private File path;
	private String filePrefix = "";
	private Collection<File> photoList = new ArrayList<File>();
	protected ICallback clb = null;

	public abstract void close();

	public void setPath(File path) {
		this.path = path;

	}

	public File getPath() {
		return this.path;
	}

	public void setMinPhotoNumber(int min) {
		this.min = min;
	}

	public int getMinPhotoNumber() {
		return this.min;
	}

	public void setMaxPhotoNumber(int max) {
		this.max = max;
	}

	public int getMaxPhotoNumber() {
		return this.max;
	}

	public String getFilePrefix() {
		return filePrefix;
	}

	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	public void addPhoto(File photoFile) {
		photoList.add(photoFile);
	}

	public Collection<File> getPhotoList() {
		return photoList;
	}

	public static final DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");

	public File getNewPhotoName(int num) throws IOException
	{
		if(clb != null) return clb.getFileName();
		Lun_Control isno=new Lun_Control();
		String s = String.format(isno.IsEmriNo+"_"+isno.TesisatNo+"_"+num+".jpg");
		return new File(path, s);

	}

	public abstract void show(ICallback clb, IFotograf foto) throws Exception;

	public interface ICallback {

		File getFileName();
	}
}
