package edu.kit.aifb.mbeantimeseriesreporter.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Attribute")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Attribute {

	protected String name;
	protected GaugeType gaugeType;
	protected MetricName metricName;

	public Attribute() {

	}

	public Attribute(String name, GaugeType gaugeType, MetricName metricName) {
		this.name = name;
		this.gaugeType = gaugeType;
		this.metricName = metricName;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(required = true, name = "GaugeType", type = GaugeType.class)
	public GaugeType getGaugeType() {
		return gaugeType;
	}

	public void setGaugeType(GaugeType gaugeType) {
		this.gaugeType = gaugeType;
	}

	@XmlElement(required = true, name = "MetricName", type = MetricName.class)
	public MetricName getMetricName() {
		return metricName;
	}

	public void setMetricName(MetricName metricName) {
		this.metricName = metricName;
	}

}
