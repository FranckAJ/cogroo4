/**
 * Copyright (C) 2012 cogroo <cogroo@cogroo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cogroo.addon.addon.conf;

import com.sun.star.beans.XHierarchicalPropertySet;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XSingleServiceFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XChangesBatch;

public class DefaultConfiguration {

	public static final String CONFIGURATION_SERVICE = "com.sun.star.configuration.ConfigurationProvider";
	public static final String CONFIGURATION_READ_ONLY_VIEW = "com.sun.star.configuration.ConfigurationAccess";
	public static final String CONFIGURATION_UPDATABLE_VIEW = "com.sun.star.configuration.ConfigurationUpdateAccess";
	protected XMultiServiceFactory configProvider;

	public DefaultConfiguration(XComponentContext context) {
		// this.context = context;
		XMultiServiceFactory factory = (XMultiServiceFactory) UnoRuntime
				.queryInterface(XMultiServiceFactory.class, context
						.getServiceManager());
		this.init(factory);
	}

	public DefaultConfiguration(XMultiServiceFactory factory) {
		this.init(factory);
	}

	protected void init(XMultiServiceFactory factory) {
		try {
			this.configProvider = (XMultiServiceFactory) UnoRuntime
					.queryInterface(XMultiServiceFactory.class, factory
							.createInstance(CONFIGURATION_SERVICE));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public Object getRootNode(String configPath, boolean readonly) {
		try {
			if (readonly) {
				com.sun.star.beans.PropertyValue path = new com.sun.star.beans.PropertyValue();
				path.Name = "nodepath";
				path.Value = configPath;

				Object[] args = new Object[1];
				args[0] = path;

				return this.configProvider.createInstanceWithArguments(
						CONFIGURATION_READ_ONLY_VIEW, args);
			} else {
				com.sun.star.beans.PropertyValue aPathArgument = new com.sun.star.beans.PropertyValue();
				aPathArgument.Name = "nodepath";
				aPathArgument.Value = configPath;

				com.sun.star.beans.PropertyValue aModeArgument = new com.sun.star.beans.PropertyValue();
				aModeArgument.Name = "EnableAsync";
				aModeArgument.Value = new Boolean(true);

				Object[] args = new Object[2];
				args[0] = aPathArgument;
				args[1] = aModeArgument;

				return this.configProvider.createInstanceWithArguments(
						CONFIGURATION_UPDATABLE_VIEW, args);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;
	}

	public Object getProperty(String configPath, String property) {
		Object theObject = null;
		try {
			Object root = this.getRootNode(configPath, true);

			XHierarchicalPropertySet props = (XHierarchicalPropertySet) UnoRuntime
					.queryInterface(XHierarchicalPropertySet.class, root);
			theObject = props.getHierarchicalPropertyValue(property);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theObject;
	}

	public Object getChildNode(Object parent, String child) {
		Object theObject = null;
		try {
			XNameAccess childNode = (XNameAccess) UnoRuntime.queryInterface(
					XNameAccess.class, parent);

			if (childNode.hasByName(child)) {

				theObject = childNode.getByName(child);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return theObject;
	}

	public void setProperty(String configPath, String property, String value) {
		try {

			com.sun.star.beans.PropertyValue aPathArgument = new com.sun.star.beans.PropertyValue();
			aPathArgument.Name = "nodepath";
			aPathArgument.Value = configPath;

			com.sun.star.beans.PropertyValue aModeArgument = new com.sun.star.beans.PropertyValue();
			aModeArgument.Name = "EnableAsync";
			aModeArgument.Value = new Boolean(true);

			Object[] args = new Object[2];
			args[0] = aPathArgument;
			args[1] = aModeArgument;

			Object xViewRoot = this.configProvider.createInstanceWithArguments(
					CONFIGURATION_UPDATABLE_VIEW, args);

			XNameAccess props = (XNameAccess) UnoRuntime.queryInterface(
					XNameAccess.class, xViewRoot);

			if (props.hasByName(property)) {
				XPropertySet properties = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, xViewRoot);

				properties.setPropertyValue(property, value);
			} else {
				// get the container
				XNameContainer setUpdate = (XNameContainer) UnoRuntime
						.queryInterface(XNameContainer.class, xViewRoot);

				// create a new detached set element (instance of
				// DataSourceDescription)
				XSingleServiceFactory elementFactory = (XSingleServiceFactory) UnoRuntime
						.queryInterface(XSingleServiceFactory.class, setUpdate);

				// the new element is the result !
				Object prop = elementFactory.createInstance();
				// insert it - this also names the element
				setUpdate.insertByName(property, prop);
			}

			// commit the changes
			this.commit(xViewRoot);

			// now clean up
			((XComponent) UnoRuntime
					.queryInterface(XComponent.class, xViewRoot)).dispose();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Commit the XChangeBatch control
	 * 
	 * @param root
	 */
	public void commit(Object root) {
		try {
			XChangesBatch xUpdateControl = (XChangesBatch) UnoRuntime
					.queryInterface(XChangesBatch.class, root);
			xUpdateControl.commitChanges();
		} catch (WrappedTargetException e) {
			e.printStackTrace();
		}
	}

	public void dispose(Object obj) {
		((XComponent) UnoRuntime.queryInterface(XComponent.class, obj))
				.dispose();
	}
}
