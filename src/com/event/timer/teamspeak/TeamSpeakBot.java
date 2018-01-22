//package com.event.timer.teamspeak;
//
//import com.github.theholywaffle.teamspeak3.TS3Api;
//import com.github.theholywaffle.teamspeak3.TS3Config;
//import com.github.theholywaffle.teamspeak3.TS3Query;
//import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.AudioDevice;
//import javazoom.jl.player.JavaSoundAudioDeviceFactory;
//
///**
// * @author Mikle Garin
// */
//
//public class TeamSpeakBot
//{
//    public static void main ( final String[] args ) throws JavaLayerException
//    {
//        AudioDevice audioDevice = new JavaSoundAudioDeviceFactory ().createAudioDevice ();
//
//        final TS3Config config = new TS3Config ();
//        //                config.setHost ( "ts.gw2nice.uk" );
//        config.setHost ( "query.khaki.cleanvoice.ru" );
//        config.setQueryPort ( 9994 /*10011*/ );
//        config.setEnableCommunicationsLogging ( true );
//
//        final TS3Query query = new TS3Query ( config );
//        query.connect ();
//
//        final TS3Api api = query.getApi ();
//        api.selectVirtualServerById ( -1 );
//        api.selectVirtualServerByPort ( 9994 );
//        api.setNickname ( "TestBot" );
//        api.usePrivilegeKey ( "93xuKgUB8XBgEIRxV0KTr8iPX9u+nEnq9C8zjzZj" );
//
//        final Channel channel = api.getChannelsByName ( "NICE sheet" ).get ( 0 );
//        api.moveClient ( api.whoAmI ().getId (), channel.getId () );
//
//        api.sendChannelMessage ( "Test bot is online!" );
//
//        // api.sendTextMessage (  )
//    }
//}