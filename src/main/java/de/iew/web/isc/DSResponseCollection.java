/*
 * Copyright 2012 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

package de.iew.web.isc;

import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementiert ein Objekt für das Versenden von Smartclient Webservice
 * Antworten.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 21.11.12 - 23:35
 */
public class DSResponseCollection extends DSResponseAbstract {
    public DSResponseCollection() {
        init();
    }

    public DSResponseCollection(Collection data) {
        init(data);
        setTotalRows(data.size());
    }

    public DSResponseCollection(Collection data, int totalRows) {
        init(data);
        setTotalRows(totalRows);
    }

    private void init() {
        setStatus(DSResponseAbstract.STATUS_SUCCESS);
        setStartRow(0);
        setEndRow(0);
        setTotalRows(0);
    }

    private void init(Collection data) {
        setStatus(DSResponseAbstract.STATUS_SUCCESS);
        setCollectionData(data);
        setStartRow(0);
        setEndRow(data.size() - 1);
        setTotalRows(data.size());
    }

    public int getStartRow() {
        return (Integer) getResponseModel().get("startRow");
    }

    public void setStartRow(int startRow) {
        getResponseModel().put("startRow", startRow);
    }

    public int getEndRow() {
        return (Integer) getResponseModel().get("endRow");
    }

    public void setEndRow(int endRow) {
        getResponseModel().put("endRow", endRow);
    }

    public int getTotalRows() {
        return (Integer) getResponseModel().get("totalRows");
    }

    public void setTotalRows(int totalRows) {
        getResponseModel().put("totalRows", totalRows);
    }

    public Collection getCollectionData() {
        ExtendedModelMap responseModel = getResponseModel();
        if (!responseModel.containsKey("data")) {
            responseModel.put("data", new ArrayList());
        }
        return (Collection) responseModel.get("data");
    }

    public void setCollectionData(Collection data) {
        Collection existingData = getClearedDataStore();
        existingData.addAll(data);
    }

    public Collection getClearedDataStore() {
        Collection existingData = getCollectionData();
        existingData.clear();
        return existingData;
    }

}
