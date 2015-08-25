/**
 * FancyImageConnector.java (FancyLayouts)
 * 
 * Copyright 2012 Vaadin Ltd, Sami Viitanen <alump@vaadin.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.alump.fancylayouts.gwt.client.connect;

import java.util.List;
import java.util.logging.Logger;

import org.vaadin.alump.fancylayouts.FancyLinkImage;
import org.vaadin.alump.fancylayouts.gwt.client.GwtFancyLinkImage;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyLinkImageState;
import org.vaadin.alump.fancylayouts.gwt.client.shared.RotateDirection;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.fancylayouts.FancyLinkImage.class)
public class FancyLinkImageConnector extends AbstractComponentConnector {

    private final FancyLinkImageClientRpc rpc = new FancyLinkImageClientRpc() {
        @Override
        public void showImage(String key) {        	
            URLReference url = getState().resources.get(key);
            if (url != null) {
                getWidget().showImage(url.getURL());
            } else {
                Logger.getLogger(
                        FancyLinkImageConnector.this.getClass().getSimpleName())
                        .severe("Resource not found!");
            }
        }
    };

    @Override
    public void init() {
        super.init();
        registerRpc(FancyLinkImageClientRpc.class, rpc);
    }

    @Override
    public GwtFancyLinkImage createWidget() {
        return new GwtFancyLinkImage();
    }

    @Override
    public GwtFancyLinkImage getWidget() {
        return (GwtFancyLinkImage) super.getWidget();
    }

    @Override
    public FancyLinkImageState getState() {
        return (FancyLinkImageState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        List<String> ids = getState().imageResIds;
        getWidget().trimImages(ids.size());

        for (int i = 0; i < ids.size(); ++i) {
            String key = ids.get(i);
            getWidget().setImage(getState().resources.get(key).getURL(), 
            		getState().resources.get(FancyLinkImage.PREF_ANCHOR + key).getURL(),
            		i);
        }

        getWidget().setAutoBrowseTimeout(getState().timeoutMs);
        getWidget().setAutoBrowseEnabled(getState().autoBrowse);
        getWidget().setFadeImages(getState().fadeTransition);
        getWidget().setRotateImages(
                getState().rotateTransition != RotateDirection.NONE,
                getState().rotateTransition == RotateDirection.HORIZONTAL);
    }
}
