package edu.kit.aifb.mbeantimeseriesreporter.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement(name = "MBeans")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MBeans {
	
	protected List<MBean> mbeans = Lists.newArrayList();

	@XmlElement(name = "MBean")
	public List<MBean> getMbeans() {
		return mbeans;
	}

	public void setMbeans(List<MBean> mbeans) {
		this.mbeans = mbeans;
	}
}