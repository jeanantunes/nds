package br.com.abril.nds.util.export;

import br.com.abril.nds.util.export.Export.Alignment;


final class CellStyleKey {
    
    private boolean even;
    private boolean lastCell;
    private Alignment alignment;
    private ColumnType columType;
    
    public CellStyleKey(final boolean even, final boolean lastCell, final Alignment alignment, final ColumnType columType) {
        super();
        this.even = even;
        this.lastCell = lastCell;
        this.alignment = alignment;
        this.columType = columType;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (alignment == null ? 0 : alignment.hashCode());
        result = prime * result + (columType == null ? 0 : columType.hashCode());
        result = prime * result + (even ? 1231 : 1237);
        result = prime * result + (lastCell ? 1231 : 1237);
        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CellStyleKey other = (CellStyleKey) obj;
        if (alignment != other.alignment) {
            return false;
        }
        if (columType != other.columType) {
            return false;
        }
        if (even != other.even) {
            return false;
        }
        if (lastCell != other.lastCell) {
            return false;
        }
        return true;
    }
    
    /**
     * @return the even
     */
    public boolean isEven() {
        return even;
    }
    
    /**
     * @param even the even to set
     */
    public void setEven(final boolean even) {
        this.even = even;
    }
    
    /**
     * @return the lastCell
     */
    public boolean isLastCell() {
        return lastCell;
    }
    
    /**
     * @param lastCell the lastCell to set
     */
    public void setLastCell(final boolean lastCell) {
        this.lastCell = lastCell;
    }
    
    /**
     * @return the alignment
     */
    public Alignment getAlignment() {
        return alignment;
    }
    
    /**
     * @param alignment the alignment to set
     */
    public void setAlignment(final Alignment alignment) {
        this.alignment = alignment;
    }
    
    /**
     * @return the columType
     */
    public ColumnType getColumType() {
        return columType;
    }
    
    /**
     * @param columType the columType to set
     */
    public void setColumType(final ColumnType columType) {
        this.columType = columType;
    }

}
