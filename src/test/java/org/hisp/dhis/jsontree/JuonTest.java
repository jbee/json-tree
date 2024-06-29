package org.hisp.dhis.jsontree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the {@link Juon} parser accessible via {@link JsonNode#ofUrlObjectNotation(String)}.
 *
 * @author Jan Bernitt
 */
class JuonTest {

    @Test
    void testBoolean() {
        assertEquals( JsonNode.of( "true" ), JsonNode.ofUrlObjectNotation( "true" ) );
        assertEquals( JsonNode.of( "false" ), JsonNode.ofUrlObjectNotation( "false" ) );
    }

    @Test
    void testBoolean_Shorthand() {
        assertEquals( JsonNode.of( "true" ), JsonNode.ofUrlObjectNotation( "t" ) );
        assertEquals( JsonNode.of( "false" ), JsonNode.ofUrlObjectNotation( "f" ) );
    }

    @Test
    void testNull() {
        assertEquals( JsonNode.of( "null" ), JsonNode.ofUrlObjectNotation( "null" ) );
    }

    @Test
    void testNull_Shorthand() {
        assertEquals( JsonNode.of( "null" ), JsonNode.ofUrlObjectNotation( "n" ) );
    }

    @Test
    @DisplayName( "In contrast to JSON an undefined, empty or blank value is considered null" )
    void testNull_Omit() {
        assertEquals( JsonNode.of( "null" ), JsonNode.ofUrlObjectNotation( null ) );
        assertEquals( JsonNode.of( "null" ), JsonNode.ofUrlObjectNotation( "" ) );
        assertEquals( JsonNode.of( "null" ), JsonNode.ofUrlObjectNotation( " " ) );
    }

    @Test
    void testNumber() {
        assertEquals( JsonNode.of( "1234" ), JsonNode.ofUrlObjectNotation( "1234" ) );
        assertEquals( JsonNode.of( "42.12" ), JsonNode.ofUrlObjectNotation( "42.12" ) );
        assertEquals( JsonNode.of( "-0.12" ), JsonNode.ofUrlObjectNotation( "-0.12" ) );
    }

    @Test
    @DisplayName( "In contrast to JSON floating point numbers can start with a dot" )
    void testNumber_OmitLeadingZero() {
        assertEquals( JsonNode.of( "0.12" ), JsonNode.ofUrlObjectNotation( ".12" ) );
    }

    @Test
    @DisplayName( "In contrast to JSON floating point numbers can end with a dot" )
    void testNumber_OmitTailingZero() {
        assertEquals( JsonNode.of( "0.0" ), JsonNode.ofUrlObjectNotation( "0." ) );
    }

    @Test
    void testArray() {
        assertEquals( JsonNode.of( "[]" ), JsonNode.ofUrlObjectNotation( "()" ) );
        assertEquals( JsonNode.of( "[1,2,3]" ), JsonNode.ofUrlObjectNotation( "(1,2,3)" ) );
        assertEquals( JsonNode.of( "[true,false]" ), JsonNode.ofUrlObjectNotation( "(true,false)" ) );
        assertEquals( JsonNode.of( "[\"a\",\"b\",\"c\",\"d\"]" ), JsonNode.ofUrlObjectNotation( "('a','b','c','d')" ) );
    }

    @Test
    void testArray_Array() {
        assertEquals( JsonNode.of( "[[]]" ), JsonNode.ofUrlObjectNotation( "(())" ) );
        assertEquals( JsonNode.of( "[[],[]]" ), JsonNode.ofUrlObjectNotation( "((),())" ) );
        assertEquals( JsonNode.of( "[[[]]]" ), JsonNode.ofUrlObjectNotation( "((()))" ) );
        assertEquals( JsonNode.of( "[[[1,2],[3,4]],[5,6]]" ), JsonNode.ofUrlObjectNotation( "(((1,2),(3,4)),(5,6))" ) );
    }

    @Test
    @DisplayName( "In contrast to JSON nulls in arrays can be omitted (left empty)" )
    void testArray_OmitNulls() {
        assertEquals( JsonNode.of( "[null,null]" ), JsonNode.ofUrlObjectNotation( "(,)" ) );
        assertEquals( JsonNode.of( "[null,null,3]" ), JsonNode.ofUrlObjectNotation( "(,,3)" ) );
        assertEquals( JsonNode.of( "[1,null,3]" ), JsonNode.ofUrlObjectNotation( "(1,,3)" ) );
        assertEquals( JsonNode.of( "[1,null,null]" ), JsonNode.ofUrlObjectNotation( "(1,,)" ) );
        assertEquals( JsonNode.of( "[1,null,0.3,null,5]" ), JsonNode.ofUrlObjectNotation( "(1,,.3,,5)" ) );
    }

    @Test
    void testObject() {
        assertEquals( JsonNode.of( "{\"hi\":\"ho\"}" ), JsonNode.ofUrlObjectNotation( "(hi:'ho')" ) );
        assertEquals( JsonNode.of( "{\"no\":1,\"surprises\":{\"please\":true}}" ),
            JsonNode.ofUrlObjectNotation( "(no:1,surprises:(please:true))" ) );
    }

    @Test
    void testMixed_Minimal() {
        JsonNode json = JsonNode.ofUrlObjectNotation( """
            (name:'John',age:42,license:false,keywords:('hello','world'),void:null)
            """ );
        String expected = """
            {"name":"John","age":42,"license":false,"keywords":["hello","world"],"void":null}
            """;
        assertEquals( JsonNode.of( expected ), json );
    }
}
