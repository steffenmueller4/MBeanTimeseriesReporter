package edu.kit.aifb.mbeantimeseriesreporter.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;

@XmlType(name = "MBean")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MBean {
	
	protected String name;
	protected String objectName;
	protected List<Attribute> attributes = Lists.newArrayList();

	public MBean() {
	}
	
	public MBean(String name, String objectName) {
		this.name = name;
		this.objectName = objectName;
	}
	
	public MBean(String name, String objectName, Attribute singleAttribute) {
		this.name = name;
		this.objectName = objectName;
		this.attributes.add(singleAttribute);
	}
	
	public MBean(String name, String objectName, List<Attribute> attributes) {
		this.name = name;
		this.objectName = objectName;
		this.attributes.addAll(attributes);
	}
	
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name="objectname")
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@XmlElement(name = "Attribute")
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
}
