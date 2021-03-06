
package com.pugh.sockso.web;

import com.pugh.sockso.music.Album;
import com.pugh.sockso.music.Artist;
import com.pugh.sockso.templates.api.TAlbums;
import com.pugh.sockso.tests.SocksoTestCase;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonWriterTest extends SocksoTestCase {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @Override
    protected void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter( stringWriter );
    }

    protected String write( String json ) throws Exception {
        jsonWriter.write( json );
        return stringWriter.toString();
    }

    public void testWhiteSpaceIsRemovedFromJson() throws Exception {
        String input = "{  id: 123, name: \"foo \\\"bar\" }";
        assertEquals( "{id:123,name:\"foo \\\"bar\"}", write(input) );
    }

    public void testIssue109() throws Exception {
        TAlbums tpl = new TAlbums();
        List<Album> albums = new ArrayList<Album>();
        final Artist artist = new Artist.Builder().id(1).name("Artist\"").build();
        albums.add( new Album.Builder()
                .artist(artist)
                .id(2)
                .name("Album\"")
                .year("1980")
                .build()
                );
        tpl.setAlbums(albums);
        tpl.makeRenderer().renderTo(jsonWriter);

        String expected = "[{\"id\":2,\"name\":\"Album\\\"\",\"date_added\":\"null\",\"artist\":{\"id\":1,\"name\":\"Artist\\\"\",\"date_added\":\"null\"}}]";
        String actual = stringWriter.toString();

        assertEquals( expected, actual );
    }
    
}
