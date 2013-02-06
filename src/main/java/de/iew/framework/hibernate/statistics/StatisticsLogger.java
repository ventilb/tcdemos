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

package de.iew.framework.hibernate.statistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Implements a very basic logger to print hibernate statistics in the log.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.02.13 - 23:47
 */
@Component
public class StatisticsLogger {

    private static final Log log = LogFactory.getLog(StatisticsLogger.class);

    @Scheduled(fixedDelay = 120000)
    public void logStatistics() {
        if (log.isDebugEnabled()) {
            log.debug("Blubb");
        }

        Statistics statistics = this.sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        StringBuilder sb = new StringBuilder("\nStatistics");
        sb.append("\nCloseStatementCount: ").append(statistics.getCloseStatementCount());

        sb.append("\nEntityDeleteCount: ").append(statistics.getEntityDeleteCount());
        sb.append("\nEntityInsertCount: ").append(statistics.getEntityInsertCount());
        sb.append("\nEntityLoadCount: ").append(statistics.getEntityLoadCount());
        sb.append("\nEntityFetchCount: ").append(statistics.getEntityFetchCount());
        sb.append("\nEntityUpdateCount: ").append(statistics.getEntityUpdateCount());
        sb.append("\nQueryExecutionCount: ").append(statistics.getQueryExecutionCount());
        sb.append("\nQueryExecutionMaxTime: ").append(statistics.getQueryExecutionMaxTime());
        sb.append("\nQueryExecutionMaxTimeQueryString: ").append(statistics.getQueryExecutionMaxTimeQueryString());
        sb.append("\nQueryCacheHitCount: ").append(statistics.getQueryCacheHitCount());
        sb.append("\nQueryCacheMissCount: ").append(statistics.getQueryCacheMissCount());
        sb.append("\nQueryCachePutCount: ").append(statistics.getQueryCachePutCount());
        sb.append("\nNaturalIdQueryExecutionCount: ").append(statistics.getNaturalIdQueryExecutionCount());
        sb.append("\nNaturalIdQueryExecutionMaxTime: ").append(statistics.getNaturalIdQueryExecutionMaxTime());
        sb.append("\nNaturalIdQueryExecutionMaxTimeRegion: ").append(statistics.getNaturalIdQueryExecutionMaxTimeRegion());
        sb.append("\nNaturalIdCacheHitCount: ").append(statistics.getNaturalIdCacheHitCount());
        sb.append("\nNaturalIdCacheMissCount: ").append(statistics.getNaturalIdCacheMissCount());
        sb.append("\nNaturalIdCachePutCount: ").append(statistics.getNaturalIdCachePutCount());
        sb.append("\nUpdateTimestampsCacheHitCount: ").append(statistics.getUpdateTimestampsCacheHitCount());
        sb.append("\nUpdateTimestampsCacheMissCount: ").append(statistics.getUpdateTimestampsCacheMissCount());
        sb.append("\nUpdateTimestampsCachePutCount: ").append(statistics.getUpdateTimestampsCachePutCount());
        sb.append("\nFlushCount: ").append(statistics.getFlushCount());
        sb.append("\nConnectCount: ").append(statistics.getConnectCount());
        sb.append("\nSecondLevelCacheHitCount: ").append(statistics.getSecondLevelCacheHitCount());
        sb.append("\nSecondLevelCacheMissCount: ").append(statistics.getSecondLevelCacheMissCount());
        sb.append("\nSecondLevelCachePutCount: ").append(statistics.getSecondLevelCachePutCount());
        sb.append("\nSessionCloseCount: ").append(statistics.getSessionCloseCount());
        sb.append("\nSessionOpenCount: ").append(statistics.getSessionOpenCount());
        sb.append("\nCollectionLoadCount: ").append(statistics.getCollectionLoadCount());
        sb.append("\nCollectionFetchCount: ").append(statistics.getCollectionFetchCount());
        sb.append("\nCollectionUpdateCount: ").append(statistics.getCollectionUpdateCount());
        sb.append("\nCollectionRemoveCount: ").append(statistics.getCollectionRemoveCount());
        sb.append("\nCollectionRecreateCount: ").append(statistics.getCollectionRecreateCount());
        sb.append("\nStartTime: ").append(statistics.getStartTime());
        sb.append("\nQueries: ").append(statistics.getQueries());
        sb.append("\nEntityNames: ").append(statistics.getEntityNames());
        sb.append("\nCollectionRoleNames: ").append(statistics.getCollectionRoleNames());
        sb.append("\nSecondLevelCacheRegionNames: ").append(statistics.getSecondLevelCacheRegionNames());
        sb.append("\nSuccessfulTransactionCount: ").append(statistics.getSuccessfulTransactionCount());
        sb.append("\nTransactionCount: ").append(statistics.getTransactionCount());
        sb.append("\nPrepareStatementCount: ").append(statistics.getPrepareStatementCount());
        sb.append("\nCloseStatementCount: ").append(statistics.getCloseStatementCount());
        sb.append("\nOptimisticFailureCount: ").append(statistics.getOptimisticFailureCount());

        if (log.isDebugEnabled()) {
            log.debug(sb);
        }

    }

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
