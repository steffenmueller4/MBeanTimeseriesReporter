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

import com.beust.jcommander.IStringConverter;

/**
 * 
 * @author Steffen Mueller
 * @version 1.0.0
 */
public class HostPortConverter implements IStringConverter<HostPort> {
	@Override
	public HostPort convert(String value) {
		String[] s = value.split(":");
		HostPort result = new HostPort(s[0], Integer.parseInt(s[1]));

		return result;
	}
}