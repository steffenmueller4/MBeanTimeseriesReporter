package edu.kit.aifb.mbeantimeseriesreporter.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "GaugeType")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GaugeType {
	
	@XmlEnum
	public enum GaugeTypeValue {
		Double,
		Long,
		Integer,
		Boolean,
		SizeOfList,
		SizeOfArray
	}
	
	protected GaugeTypeValue value;
	protected String operation;
	
	public GaugeType() {
		
	}
	
	public GaugeType(GaugeTypeValue value){
		this.value = value;
	}

	@XmlValue
	public GaugeTypeValue getValue() {
		return value;
	}

	public void setValue(GaugeTypeValue value) {
		this.value = value;
	}
}
