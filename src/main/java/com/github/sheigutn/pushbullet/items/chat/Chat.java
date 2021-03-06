package com.github.sheigutn.pushbullet.items.chat;

import com.github.sheigutn.pushbullet.http.defaults.delete.DeleteSpecificChatRequest;
import com.github.sheigutn.pushbullet.http.defaults.post.BlockUserRequest;
import com.github.sheigutn.pushbullet.interfaces.Blockable;
import com.github.sheigutn.pushbullet.items.PushbulletObject;
import com.github.sheigutn.pushbullet.items.push.sent.Push;
import com.github.sheigutn.pushbullet.http.defaults.post.UpdateChatMuteStatusRequest;
import com.github.sheigutn.pushbullet.interfaces.Deletable;
import com.github.sheigutn.pushbullet.interfaces.Mutable;
import com.github.sheigutn.pushbullet.interfaces.Pushable;
import com.github.sheigutn.pushbullet.items.push.sendable.SendablePush;
import com.github.sheigutn.pushbullet.items.user.ChatUser;
import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Chat extends PushbulletObject implements Deletable, Mutable, Pushable, Blockable {

    /**
     * The user this chat is held with
     */
    private ChatUser with;

    /**
     * Whether this chat is muted
     */
    private boolean muted;

    @Override
    public void delete() {
        if(!isActive()) return;
        DeleteSpecificChatRequest chatRequest = new DeleteSpecificChatRequest(getIdentity());
        getPushbullet().executeRequest(chatRequest);
        setActive(false);
    }

    public void mute() {
        updateMute(true);
    }

    public void unmute() {
        updateMute(false);
    }

    /**
     * Used to block the user this chat is held with
     */
    @Override
    public void block() {
        if(!isActive()) return;
        getPushbullet().executeRequest(new BlockUserRequest(getWith().getEmail()));
        setActive(false);
    }

    private void updateMute(boolean muteStatus) {
        getPushbullet().executeRequest(new UpdateChatMuteStatusRequest(getIdentity(), muteStatus));
        muted = muteStatus;
    }

    @Override
    public Push push(SendablePush push) {
        return with.push(push);
    }
}
