package ru.ifmo.cs.semnet.importer.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.ifmo.cs.semnet.core.TypeLink;
import ru.ifmo.cs.semnet.importer.ImportPackage;

public class ImportPackageImpl implements ImportPackage {

	private Map<String, Object> inputParameters;
	
	private String view;

	private Locale locale;
	
	private Map<String, TypeLink> linkCandidates;
	
	private String parentView;
	
	public ImportPackageImpl() {
		inputParameters = new HashMap<>();
	}

	@Override
	public String getView() {
		return view;
	}

	@Override
	public void setView(String view) {
		this.view = view;
	}

	@Override
	public Map<String, TypeLink> getLinkCandidates() {
		return linkCandidates;
	}
	
	@Override
	public void addLinkCandidate(String viewLinkageNode, TypeLink link) {
		linkCandidates.put(viewLinkageNode, link);
	}

	@Override
	public String getParentView() {
		return parentView;
	}

	@Override
	public void setParentView(String parentView) {
		this.parentView = parentView;
	}

	@Override
	public Map<String, Object> getInitParameters() {
		return inputParameters;
	}

	@Override
	public void addInitParameter(String name, Object value) {
		if(name != null && value != null) {
			inputParameters.put(name, value);
		}
	}

	@Override
	public Locale getLocaleForView() {
		return locale;
	}

	@Override
	public void setLocaleForView(Locale locale) {
		this.locale = locale;
	}
}
