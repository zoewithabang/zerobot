package com.github.zoewithabang.task;

import com.github.zoewithabang.bot.IBot;
import com.github.zoewithabang.model.CyTubeMedia;
import com.github.zoewithabang.util.CyTubeHelper;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.DiscordException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class CyTubeNowPlayingPresence implements ITask
{
    public static String TASK = "cytubeNp";
    private IBot bot;
    private Properties botProperties;
    private File log;
    private String latestNowPlaying;
    
    public CyTubeNowPlayingPresence(IBot bot, Properties botProperties)
    {
        this.bot = bot;
        this.botProperties = botProperties;
        log = new File(botProperties.getProperty("cytubeloglocation"));
        latestNowPlaying = "";
    }
    
    @Override
    public void run()
    {
        try
        {
            CyTubeMedia nowPlaying = CyTubeHelper.getLatestNowPlaying(log);
            String title = nowPlaying.getTitle();
            
            //if found title is different to stored, update stored and bot presence
            if(!latestNowPlaying.equals(title))
            {
                LOGGER.debug("Now playing: {}", title);
                latestNowPlaying = title;
                bot.updatePresence(StatusType.ONLINE, ActivityType.LISTENING, latestNowPlaying);
            }
        }
        catch(IOException e)
        {
            LOGGER.error("Could not find the channel log location '{}'.", botProperties.getProperty("cytubeloglocation"), e);
        }
        catch(IllegalStateException e)
        {
            LOGGER.error("IllegalStateException in getting latest now playing CyTube media.");
        }
        catch(DiscordException e)
        {
            LOGGER.warn("DiscordException on updating now playing presence, probably not logged in and ready?");
        }
    }
}
