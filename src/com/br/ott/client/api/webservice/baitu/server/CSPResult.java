/**
 * CSPResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.server;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CSPResult implements java.io.Serializable {
	private int result;

	private String errorDescription;

	public CSPResult() {
	}

	public CSPResult(int result, String errorDescription) {
		this.result = result;
		this.errorDescription = errorDescription;
	}

	/**
	 * Gets the result value for this CSPResult.
	 * 
	 * @return result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * Sets the result value for this CSPResult.
	 * 
	 * @param result
	 */
	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * Gets the errorDescription value for this CSPResult.
	 * 
	 * @return errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Sets the errorDescription value for this CSPResult.
	 * 
	 * @param errorDescription
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	private Object __equalsCalc = null;

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CSPResult))
			return false;
		CSPResult other = (CSPResult) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& this.result == other.getResult()
				&& ((this.errorDescription == null && other
						.getErrorDescription() == null) || (this.errorDescription != null && this.errorDescription
						.equals(other.getErrorDescription())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		_hashCode += getResult();
		if (getErrorDescription() != null) {
			_hashCode += getErrorDescription().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static TypeDesc typeDesc = new TypeDesc(CSPResult.class, true);

	static {
		typeDesc.setXmlType(new QName("urn:ItvServiceImpl", "CSPResult"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("result");
		elemField.setXmlName(new QName("", "Result"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema",
				"int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("errorDescription");
		elemField.setXmlName(new QName("", "ErrorDescription"));
		elemField.setXmlType(new QName(
				"http://schemas.xmlsoap.org/soap/encoding/", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static Serializer getSerializer(String mechType, Class _javaType,
			QName _xmlType) {
		return new BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static Deserializer getDeserializer(String mechType,
			Class _javaType, QName _xmlType) {
		return new BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
