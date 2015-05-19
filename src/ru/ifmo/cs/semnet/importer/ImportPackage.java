package ru.ifmo.cs.semnet.importer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.TypeLink;

public class ImportPackage<T extends Node> {

	private Map<String, Object> inputParameters;
	
	private String view;

	private Locale locale;
	
	private Map<String, TypeLink> linkCandidates;
	
	private String parentView;
	
	public ImportPackage() {
		inputParameters = new HashMap<>();
	}
	
	public Map<String, Object> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(Map<String, Object> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Map<String, TypeLink> getLinkCandidates() {
		return linkCandidates;
	}

	public void setLinkCandidates(Map<String, TypeLink> links) {
		linkCandidates = links;
	}

	public void addParameter(String s, Object value) {
		inputParameters.put(s, value);
	}
	
	public void addLinkCandidate(String viewLinkageNode, TypeLink link) {
		linkCandidates.put(viewLinkageNode, link);
	}

	public String getParentView() {
		return parentView;
	}

	public void setParentView(String parentView) {
		this.parentView = parentView;
	}
}
