/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.listener.persistence.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.listener.persistence.dao.RecordStatusDao;
import org.orcid.listener.persistence.entities.RecordStatusEntity;
import org.orcid.listener.persistence.util.AvailableBroker;
import org.orcid.test.OrcidJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OrcidJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:orcid-message-listener-test-context.xml" })
public class RecordStatusManagerTest {

	@Resource
	private RecordStatusManager recordStatusManager;
	
	@Resource
	private RecordStatusDao recordStatusDao;
	
	@Test
	public void markAsSentTest() {
		String orcid = "0000-0000-0000-0001";
		assertFalse(recordStatusDao.exists(orcid));
		recordStatusManager.markAsSent(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		assertTrue(recordStatusDao.exists(orcid));
		RecordStatusEntity entity = recordStatusDao.get(orcid);
		assertNotNull(entity);
		assertEquals(orcid, entity.getId());
		assertEquals(Integer.valueOf(0), entity.getDumpStatus12Api());
	}
	
	@Test
	public void markAsFailedTest() {
		String orcid = "0000-0000-0000-0002";
		assertFalse(recordStatusDao.exists(orcid));
		recordStatusManager.markAsFailed(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		assertTrue(recordStatusDao.exists(orcid));
		RecordStatusEntity entity = recordStatusDao.get(orcid);
		assertNotNull(entity);
		assertEquals(orcid, entity.getId());
		assertEquals(Integer.valueOf(1), entity.getDumpStatus12Api());
		recordStatusManager.markAsFailed(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		entity = recordStatusDao.get(orcid);
		assertNotNull(entity);
		assertEquals(orcid, entity.getId());
		assertEquals(Integer.valueOf(2), entity.getDumpStatus12Api());
		recordStatusManager.markAsFailed(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		entity = recordStatusDao.get(orcid);
		assertNotNull(entity);
		assertEquals(orcid, entity.getId());
		assertEquals(Integer.valueOf(3), entity.getDumpStatus12Api());
	}
	
	@Test
	public void setSentThenFailedThenSentAgainTest() {		
		String orcid = "0000-0000-0000-0003";
		//First mark it as sent
		assertFalse(recordStatusDao.exists(orcid));
		recordStatusManager.markAsSent(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		assertTrue(recordStatusDao.exists(orcid));
		RecordStatusEntity entity = recordStatusDao.get(orcid);
		assertNotNull(entity);
		assertEquals(orcid, entity.getId());
		assertEquals(Integer.valueOf(0), entity.getDumpStatus12Api());
		
		//Then make it fail 3 times
		recordStatusManager.markAsFailed(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		recordStatusManager.markAsFailed(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		recordStatusManager.markAsFailed(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		//Verify it's status is 3
		entity = recordStatusDao.get(orcid);
		assertNotNull(entity);
		assertEquals(orcid, entity.getId());
		assertEquals(Integer.valueOf(3), entity.getDumpStatus12Api());
		
		//Then mark it as sent
		recordStatusManager.markAsSent(orcid, AvailableBroker.DUMP_STATUS_1_2_API);
		//Verify it's status was cleared 
		entity = recordStatusDao.get(orcid);
		assertNotNull(entity);
		assertEquals(orcid, entity.getId());
		assertEquals(Integer.valueOf(0), entity.getDumpStatus12Api());
	}
}
