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

package de.iew.web.isc;

/**
 * Implements a simple bean to encapsulate Smartclient request meta data.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.02.13 - 23:40
 */
public class DSRequest {

    private String _operationType;

    private Long _startRow;

    private Long _endRow;

    private String _textMatchStyle;

    private String _componentId;

    private String _dataSource;

    private String isc_metaDataPrefix;

    private String isc_dataFormat;

    public Long getStartRow() {
        return _startRow;
    }

    public void setStartRow(Long _startRow) {
        this._startRow = _startRow;
    }

    public Long getEndRow() {
        return _endRow;
    }

    public void setEndRow(Long _endRow) {
        this._endRow = _endRow;
    }

    public String get_operationType() {
        return _operationType;
    }

    public long getRowCount() {
        return getEndRow() - getStartRow();
    }

    public void set_operationType(String _operationType) {
        this._operationType = _operationType;
    }

    public Long get_startRow() {
        return _startRow;
    }

    public void set_startRow(Long _startRow) {
        this._startRow = _startRow;
    }

    public Long get_endRow() {
        return _endRow;
    }

    public void set_endRow(Long _endRow) {
        this._endRow = _endRow;
    }

    public String get_textMatchStyle() {
        return _textMatchStyle;
    }

    public void set_textMatchStyle(String _textMatchStyle) {
        this._textMatchStyle = _textMatchStyle;
    }

    public String get_componentId() {
        return _componentId;
    }

    public void set_componentId(String _componentId) {
        this._componentId = _componentId;
    }

    public String get_dataSource() {
        return _dataSource;
    }

    public void set_dataSource(String _dataSource) {
        this._dataSource = _dataSource;
    }

    public String getIsc_metaDataPrefix() {
        return isc_metaDataPrefix;
    }

    public void setIsc_metaDataPrefix(String isc_metaDataPrefix) {
        this.isc_metaDataPrefix = isc_metaDataPrefix;
    }

    public String getIsc_dataFormat() {
        return isc_dataFormat;
    }

    public void setIsc_dataFormat(String isc_dataFormat) {
        this.isc_dataFormat = isc_dataFormat;
    }
}
