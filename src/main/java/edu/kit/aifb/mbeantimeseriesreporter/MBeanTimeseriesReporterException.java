/**
 * Copyright 2016 Steffen Mueller
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.aifb.mbeantimeseriesreporter;

/**
 * Exception
 * 
 * @author Steffen Mueller
 * @version 1.0.0
 */
public class MBeanTimeseriesReporterException extends Exception {

	/**
	 * {@link java.io.Serializable}
	 */
	private static final long serialVersionUID = -183813931327618399L;

	public MBeanTimeseriesReporterException() {
		super();
	}

	public MBeanTimeseriesReporterException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MBeanTimeseriesReporterException(String message, Throwable cause) {
		super(message, cause);
	}

	public MBeanTimeseriesReporterException(String message) {
		super(message);
	}

	public MBeanTimeseriesReporterException(Throwable cause) {
		super(cause);
	}

}
