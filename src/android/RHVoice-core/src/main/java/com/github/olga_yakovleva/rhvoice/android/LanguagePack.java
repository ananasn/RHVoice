/* Copyright (C) 2017  Olga Yakovleva <yakovleva.o.v@gmail.com> */

/* This program is free software: you can redistribute it and/or modify */
/* it under the terms of the GNU Lesser General Public License as published by */
/* the Free Software Foundation, either version 3 of the License, or */
/* (at your option) any later version. */

/* This program is distributed in the hope that it will be useful, */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the */
/* GNU Lesser General Public License for more details. */

/* You should have received a copy of the GNU Lesser General Public License */
/* along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package com.github.olga_yakovleva.rhvoice.android;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class LanguagePack extends DataPack
{
    private final String code;
    private final List<VoicePack> voices=new ArrayList<VoicePack>();

    public LanguagePack(String name,String code,int format,int revision)
    {
        super(name,format,revision);
        this.code=code;
}

    public LanguagePack(String name,String code,int format,int revision,String tempLink)
    {
        super(name,format,revision,tempLink);
        this.code=code;
}

    public String getType()
    {
        return "language";
}

    public String getDisplayName()
    {
        Locale loc=new Locale(code);
        return loc.getDisplayLanguage();
}

    protected String getBaseFileName()
    {
        return String.format("RHVoice-language-%s",getName());
}

    public List<VoicePack> getVoices()
    {
        return voices;
}

    public LanguagePack addVoice(VoicePack voice)
    {
        voices.add(voice);
        return this;
}

    @Override
    public boolean getEnabled(Context context)
    {
        for(VoicePack voice: voices)
            {
                if(voice.getEnabled(context))
                    return true;
}
        return false;
}

    @Override
    public boolean sync(Context context,IDataSyncCallback callback)
    {
        boolean done=true;
        for(VoicePack voice: voices)
            {
                if(!voice.sync(context,callback))
                    done=false;
}
        done&=super.sync(context,callback);
        return done;
}

    public List<String> getPaths(Context context)
    {
        List<String> paths=new ArrayList<String>();
        String languagePath=getPath(context);
        if(languagePath==null)
            return paths;
        for(VoicePack voice: voices)
            {
                if(!voice.getEnabled(context))
                    continue;
                String voicePath=voice.getPath(context);
                if(voicePath!=null)
                    paths.add(voicePath);
}
        if(!paths.isEmpty())
            paths.add(languagePath);
        return paths;
}

    public String getCode()
    {
        return code;
}

    @Override
    protected void notifyDownloadStart(IDataSyncCallback callback)
    {
        callback.onLanguageDownloadStart(this);
}

    @Override
    protected void notifyDownloadDone(IDataSyncCallback callback)
    {
        callback.onLanguageDownloadDone(this);
}

    @Override
    public boolean isSyncRequired(Context context)
    {
        if(super.isSyncRequired(context))
            return true;
        for(VoicePack voice: voices)
            {
                if(voice.isSyncRequired(context))
                    return true;
}
        return false;
}
}
