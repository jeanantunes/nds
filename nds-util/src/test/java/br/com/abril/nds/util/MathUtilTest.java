package br.com.abril.nds.util;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

public class MathUtilTest {
    
    @Test
    public void calculatePercentageValueTest() {
        BigDecimal actual = MathUtil.calculatePercentageValue(new BigDecimal(100), BigDecimal.TEN);
        Assert.assertEquals(BigDecimal.TEN, actual);
        
        actual = MathUtil.calculatePercentageValue(new BigDecimal(100), BigDecimal.ZERO);
        Assert.assertEquals(BigDecimal.ZERO, actual);
        
        actual = MathUtil.calculatePercentageValue(new BigDecimal(100), new BigDecimal(100));
        Assert.assertEquals(new BigDecimal(100), actual);
    }
    
    @Test
    public void calculatePercentageValueNullValueTest() {
        BigDecimal actual = MathUtil.calculatePercentageValue(null, BigDecimal.TEN);
        Assert.assertNull(actual);
    }
    
    @Test
    public void calculatePercentageValueNullPercentageTest() {
        BigDecimal actual = MathUtil.calculatePercentageValue(null, BigDecimal.TEN);
        Assert.assertNull(actual);
    }
    
    @Test
    public void calculatePercentageValueNullParametersTest() {
        BigDecimal actual = MathUtil.calculatePercentageValue(null, null);
        Assert.assertNull(actual);
    }

}
