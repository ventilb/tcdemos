/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iew.services.impl;

import de.iew.domain.principals.Account;
import de.iew.domain.sketchpad.Polygon;
import de.iew.domain.sketchpad.RgbColor;
import de.iew.domain.sketchpad.Stroke;
import de.iew.persistence.PolygonDao;
import de.iew.persistence.RgbColorDao;
import de.iew.persistence.SketchPadDao;
import de.iew.persistence.StrokeDao;
import de.iew.services.SketchPadService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Testfälle für die {@link SketchPadServiceImpl}-Implementierung.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 16:06
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application.xml", "classpath:spring-db-config.xml", "classpath:spring-security-config.xml", "classpath:spring-acl-config.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional // Wichtig, sonst haben wir hier keine Transaktion und die Testdaten werden nicht zurück gerollt.
public class TreeSketchPadServiceImplIntegrationTest {

    @Autowired
    private SketchPadDao sketchPadDao;

    @Autowired
    private RgbColorDao sketchPadColorDao;

    @Autowired
    private StrokeDao sketchPadStrokeDao;

    @Autowired
    private PolygonDao polygonDao;

    @Autowired
    private ApplicationContext applicationContext;

    private UsernamePasswordAuthenticationToken user1;

    private UsernamePasswordAuthenticationToken user2;


    @Before
    public void setup() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SKETCHPAD_USER"));

        Account account = new Account();
        account.setUsername("SketchPad User 1");
        account.setLocked(false);
        account.setEnabled(true);

        this.user1 = new UsernamePasswordAuthenticationToken(account, "anonymous", grantedAuthorities);
        securityContext.setAuthentication(this.user1);

        account = new Account();
        account.setUsername("SketchPad User 2");
        account.setLocked(false);
        account.setEnabled(true);
        this.user2 = new UsernamePasswordAuthenticationToken(account, "anonymous", grantedAuthorities);
    }

    @After
    public void teardown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testListAllColors() throws Exception {
        SketchPadService sketchPadService = (SketchPadService) this.applicationContext.getBean("sketchPadService");

        List<RgbColor> colors = sketchPadService.listAllColors(1);
        assertEquals(5, colors.size());
    }

    @Test
    public void testListAllStrokes() {
        SketchPadService sketchPadService = (SketchPadService) this.applicationContext.getBean("sketchPadService");

        List<Stroke> strokes = sketchPadService.listAllStrokes(1);
        assertEquals(5, strokes.size());
    }

    @Test
    public void testCreatePolygon() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication sketchPadUser = securityContext.getAuthentication();

        SketchPadService sketchPadService = (SketchPadService) this.applicationContext.getBean("sketchPadService");

        Polygon polygon = sketchPadService.createPolygon(sketchPadUser, 1, 100, 150, 1, 1);
        assertNotNull(polygon.getId());

        assertEquals(this.sketchPadDao.findById(1), polygon.getSketchPad());
        assertEquals(this.sketchPadColorDao.findById(1), polygon.getLineColor());
        assertEquals(this.sketchPadStrokeDao.findById(1), polygon.getStroke());
        assertEquals(1, polygon.getSegments().size());
    }

    @Test
    public void testExtendPolygon() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication sketchPadUser = securityContext.getAuthentication();

        SketchPadService sketchPadService = (SketchPadService) this.applicationContext.getBean("sketchPadService");

        Polygon polygon = sketchPadService.createPolygon(sketchPadUser, 1, 100, 150, 1, 1);
        assertEquals(1, polygon.getSegments().size());
        assertEquals(0, polygon.getSegments().get(0).getOrdinalNumber());

        boolean extendResult = sketchPadService.extendPolygon(sketchPadUser, polygon.getId(), 160, 160);

        assertTrue(extendResult);
        assertEquals(2, polygon.getSegments().size());
        assertEquals(1, polygon.getSegments().get(1).getOrdinalNumber());

        extendResult = sketchPadService.extendPolygon(sketchPadUser, polygon.getId(), 170, 160);

        assertTrue(extendResult);
        assertEquals(3, polygon.getSegments().size());
        assertEquals(2, polygon.getSegments().get(2).getOrdinalNumber());
    }

    @Test
    public void testClosePolygon() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication sketchPadUser = securityContext.getAuthentication();

        SketchPadService sketchPadService = (SketchPadService) this.applicationContext.getBean("sketchPadService");

        Polygon polygon = sketchPadService.createPolygon(sketchPadUser, 1, 100, 150, 1, 1);

        sketchPadService.closePolygon(sketchPadUser, polygon.getId(), 200, 200);


        assertEquals(Polygon.State.CLOSED, this.polygonDao.findById(polygon.getId()).getState());
        assertEquals(2, this.polygonDao.findById(polygon.getId()).getSegments().size());
    }

    @Test
    public void testListAllPolygons() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication sketchPadUser = securityContext.getAuthentication();

        SketchPadService sketchPadService = (SketchPadService) this.applicationContext.getBean("sketchPadService");

        sketchPadService.createPolygon(sketchPadUser, 1, 100, 150, 1, 1);
        sketchPadService.createPolygon(this.user2, 1, 100, 150, 1, 1);

        List<Polygon> polygons = sketchPadService.listAllPolygons(1);
        assertEquals(1, polygons.size());
    }
}
