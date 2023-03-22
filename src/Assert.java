public class Assert {
    public static void assertTrue(boolean condition) throws Exception {
        if (!condition) {
            fail();
        }
    }

    public static void assertFalse(boolean condition) throws Exception {
        assertTrue(!condition);
    }

    /**
     * Fails a test with no message.
     */
    public static void fail() throws Exception{
        throw new Exception("Assertion failed, condition is false");
    }

    public static void failNotEquals(Object o, Object a){
        throw new RuntimeException("Those objects are not equal: " + o.toString() + " and " + a.toString());
    }

    public static void assertEquals(Object expected, Object actual) {
        if (equalsRegardingNull(expected, actual)) {
            return;
        }
        else {
            failNotEquals(expected, actual);
        }
    }

    private static boolean equalsRegardingNull(Object expected, Object actual) {
        if (expected == null) {
            return actual == null;
        }

        return isEquals(expected, actual);
    }

    private static boolean isEquals(Object expected, Object actual) {
        return expected.equals(actual);
    }

    public static void assertArrayEquals(Object[] expecteds, Object[] actuals) throws Exception {
        if(expecteds.length!=actuals.length){
            fail();
        }
        for(int i = 0; i<expecteds.length; i++){
            assertEquals(expecteds[i], actuals[i]);
        }
    }
 }
