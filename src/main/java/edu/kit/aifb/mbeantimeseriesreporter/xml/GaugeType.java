package edu.kit.aifb.mbeantimeseriesreporter.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "GaugeType")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GaugeType {
	
	protected String value;
	
	public GaugeType() {
		
	}
	
	public GaugeType(String value){
		this.value = value;
	}

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
