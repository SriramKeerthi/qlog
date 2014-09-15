package com.raremile.qlog.entities;

public class LogBuffer
{
    private String[] buffer;
    private int index;
    private int totalLines;


    public LogBuffer( int bufferLength )
    {
        buffer = new String[bufferLength];
        index = 0;
        totalLines = 0;
    }


    public void addLine( String line )
    {
        buffer[index] = line;
        index++;
        index %= buffer.length;
        totalLines++;
    }


    public String[] getLines()
    {
        int numLines = totalLines > buffer.length ? buffer.length : totalLines;
        String[] lines = new String[numLines];
        for ( int i = 0; i < numLines; i++ ) {
            lines[i] = buffer[( i + index ) % buffer.length];
        }
        return lines;
    }


    public int totalLines()
    {
        return totalLines;
    }
}
