package ru.ifmo.cs.semnet.importer.impl;

import java.io.ObjectInputStream;

import ru.ifmo.cs.semnet.core.DefaultNode;
import ru.ifmo.cs.semnet.importer.ImportDriver;

public class DefaultFileImportDriver implements ImportDriver<DefaultNode> {

	private static final long serialVersionUID = 4928270907055762930L;

	public DefaultFileImportDriver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hasUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DefaultNode getNextNodeItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCountNewEntries() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ObjectInputStream getNodesStream() {
		// TODO Auto-generated method stub
		return null;
	}

}
