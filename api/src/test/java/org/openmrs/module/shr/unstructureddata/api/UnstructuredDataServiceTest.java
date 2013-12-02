package org.openmrs.module.shr.unstructureddata.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.shr.unstructureddata.exception.AlreadyRegisteredException;
import org.openmrs.module.shr.unstructureddata.dao.UnstructuredDAO;
import org.openmrs.module.shr.unstructureddata.api.UnstructuredDataService;
import org.openmrs.test.BaseModuleContextSensitiveTest;


	public class  UnstructuredDataServiceTest extends BaseModuleContextSensitiveTest {
		
		@Test
		public void shouldSetupContext() {
			assertNotNull(Context.getService(UnstructuredDataService.class));
		}
		
		private UnstructuredDataService getService() {
			UnstructuredDataService uds = Context.getService(UnstructuredDataService.class);
			uds.deregisterUnstructuredDAO("text/plain");
			uds.onStartup();
			return uds;
		}

		/**
		 * @see UnstructuredDataService#deregisterUnstructuredDAO(String)
		 * @verifies Deregister the handler assigned for to specified contentType
		 */
		@Test
		public void deregisterUnstructuredDAO_shouldDeregisterTheDAOAssignedForToSpecifiedContentType()
				throws Exception {
			UnstructuredDataService uds = getService();
			
			UnstructuredDAO mockDAO = mock(UnstructuredDAO.class);
			
			uds.registerUnstructuredDAO("text/plain", mockDAO);
			assertTrue(uds.getUnstructuredDAO("text/plain") == mockDAO);
			
			uds.deregisterUnstructuredDAO("text/plain");
			assertTrue(uds.getUnstructuredDAO("text/plain") != mockDAO);
		}

		/**
		 * @see UnstructuredDataService#deregisterUnstructuredDAO(String)
		 * @verifies Do nothing if there is no handler assigned for a specified contentType
		 */
		@Test
		public void deregisterUnstructuredDAO_shouldDoNothingIfThereIsNoDAOAssignedForASpecifiedContentType()
				throws Exception {
			UnstructuredDataService uds = getService();
			
			uds.deregisterUnstructuredDAO("application/nothing-here");
		}

		/**
		 * @see UnstructuredDataService#getUnstructuredDAO(String)
		 * @verifies Get an appropriate content handler for a specified content type
		 */
		@Test
		public void getUnstructuredDAO_shouldGetAnAppropriateUnstructuredDAOForASpecifiedContentType()
				throws Exception {
			UnstructuredDataService uds = getService();
			
			UnstructuredDAO mockDAO = mock(UnstructuredDAO.class);
			
			uds.registerUnstructuredDAO("text/plain", mockDAO);
			assertTrue(uds.getUnstructuredDAO("text/plain") == mockDAO);
		}

		/**
		 * @see UnstructuredDataService#getUnstructuredDAO(String)
		 * @verifies Return the default handler (UnstructuredDataHandler) for an unknown content type
		 */
		@Test
		public void getUnstructuredDAO_shouldReturnTheDefaultUnstructuredDAOrForAnUnknownContentType()
				throws Exception {
			UnstructuredDataService uds = getService();
			
			assertTrue(uds.getUnstructuredDAO("application/nothing-here") instanceof UnstructuredDAO);
		}

		/**
		 * @see UnstructuredDataService#getUnstructuredDAO(String)
		 * @verifies Never return null
		 */
		@Test
		public void getUnstructuredDAO_shouldNeverReturnNull() throws Exception {
			UnstructuredDataService uds = getService();
			
			assertNotNull(uds.getUnstructuredDAO("text/plain"));
			
			UnstructuredDAO mockDAO = mock(UnstructuredDAO.class);
			uds.registerUnstructuredDAO("text/plain", mockDAO);
			
			assertNotNull(uds.getUnstructuredDAO("text/plain"));
			
			assertNotNull(uds.getUnstructuredDAO("application/xml"));
			assertNotNull(uds.getUnstructuredDAO("application/nothing-here"));
		}

		/**
		 * @see UnstructuredDataService#registerUnstructuredDAO(String,UnstructuredDAO)
		 * @verifies Register the specified handler for the specified content type
		 */
		@Test
		public void registerUnstructuredDAO_shouldRegisterTheSpecifiedHandlerForTheSpecifiedContentType()
				throws Exception {
			UnstructuredDataService uds = getService();
			
			UnstructuredDAO mockDAO = mock(UnstructuredDAO.class);
			
			assertTrue(uds.getUnstructuredDAO("text/plain") != mockDAO);
			uds.registerUnstructuredDAO("text/plain", mockDAO);
			assertTrue(uds.getUnstructuredDAO("text/plain") == mockDAO);
		}

		/**
		 * @see UnstructuredDataService#registerUnstructuredDAO(String,UnstructuredDAO)
		 * @verifies Throw an AlreadyRegisteredException if a handler is already registered for a specified content type
		 */
		@Test
		public void registerUnstructuredDAO_shouldThrowAnAlreadyRegisteredExceptionIfAHandlerIsAlreadyRegisteredForASpecifiedContentType()
				throws Exception {
			UnstructuredDataService uds = getService();
			
			UnstructuredDAO mockDAO = mock(UnstructuredDAO.class);

			UnstructuredDAO mockDAO2 = mock(UnstructuredDAO.class);
			
			uds.registerUnstructuredDAO("text/plain", mockDAO);
			try {
				uds.registerUnstructuredDAO("text/plain", mockDAO2);
				fail();
			} catch (AlreadyRegisteredException ex) {
				//expected
			}
		}

		

		/**
		 * @see UnstructuredDataService#registerUnstructuredDAO(String,UnstructuredDAO)
		 * @verifies Throw a NullPointerException if prototype is null
		 */
		@Test
		public void registerUnstructuredDAO_shouldThrowANullPointerExceptionIfPrototypeIsNull()
				throws Exception {
			UnstructuredDataService uds = getService();
			
			try {
				uds.registerUnstructuredDAO("text/plain", null);
				fail();
			} catch (NullPointerException ex) {
				//expected
			}
		}
	}
