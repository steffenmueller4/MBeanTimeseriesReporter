package edu.kit.aifb.mbeantimeseriesreporter.xml;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import edu.kit.aifb.mbeantimeseriesreporter.xml.Attribute;
import edu.kit.aifb.mbeantimeseriesreporter.xml.GaugeType;
import edu.kit.aifb.mbeantimeseriesreporter.xml.MBean;
import edu.kit.aifb.mbeantimeseriesreporter.xml.MBeans;
import edu.kit.aifb.mbeantimeseriesreporter.xml.MetricName;

public class XmlTest {

	@Test
	public void testXmlMarshall() throws JAXBException {
		GaugeType doubleType = new GaugeType("Double");
		GaugeType longType = new GaugeType("Long");

		MetricName mn1 = new MetricName("SystemCpuLoad");
		MetricName mn2 = new MetricName("ProcessCpuLoad");
		MetricName mn3 = new MetricName("Uptime");

		Attribute a1 = new Attribute("SystemCpuLoad", doubleType, mn1);
		Attribute a2 = new Attribute("ProcessCpuLoad", doubleType, mn2);
		Attribute a3 = new Attribute("Uptime", longType, mn3);

		MBean m1 = new MBean("OperatingSystem", "java.lang:type=OperatingSystem");
		m1.getAttributes().add(a1);
		m1.getAttributes().add(a2);

		MBean m2 = new MBean("Runtime", "java.lang:type=Runtime");
		m2.getAttributes().add(a3);

		MBeans mbeans = new MBeans();
		mbeans.getMbeans().add(m1);
		mbeans.getMbeans().add(m2);

		JAXBContext jaxbContext = JAXBContext.newInstance(MBeans.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(mbeans, System.out);
		jaxbMarshaller.marshal(mbeans, new File("c:/temp/mbeans.xml"));
	}
	
	@Test
	public void testXmlUnMarshall() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(MBeans.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		MBeans mbeans = (MBeans) jaxbUnmarshaller.unmarshal( new File("c:/temp/mbeans.xml") );
		
		for(MBean mbean : mbeans.getMbeans())
		{
			System.out.println(mbean.getObjectName());
			List<Attribute> attr = mbean.getAttributes();
			for(Attribute a : attr)
				System.out.println(a.getName());
		}
	}
}
