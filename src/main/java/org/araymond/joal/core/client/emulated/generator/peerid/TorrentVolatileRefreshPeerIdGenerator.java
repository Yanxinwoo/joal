package org.araymond.joal.core.client.emulated.generator.peerid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turn.ttorrent.common.protocol.TrackerMessage.AnnounceRequestMessage.RequestEvent;
import org.araymond.joal.core.client.emulated.generator.peerid.generation.PeerIdAlgorithm;
import org.araymond.joal.core.torrent.torrent.InfoHash;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by raymo on 16/07/2017.
 */
public class TorrentVolatileRefreshPeerIdGenerator extends PeerIdGenerator {
    private final Map<InfoHash, String> peerIdPerTorrent;

    @JsonCreator
    TorrentVolatileRefreshPeerIdGenerator(
            @JsonProperty(value = "algorithm", required = true) final PeerIdAlgorithm algorithm,
            @JsonProperty(value = "shouldUrlEncode", required = true) final boolean isUrlEncoded
    ) {
        super(algorithm, isUrlEncoded);
        peerIdPerTorrent = new HashMap<>();
    }

    @Override
    public String getPeerId(final InfoHash infoHash, final RequestEvent event) {
        final String peerId;

        if (!this.peerIdPerTorrent.containsKey(infoHash)) {
            this.peerIdPerTorrent.put(infoHash, super.generatePeerId());
        }

        peerId = this.peerIdPerTorrent.get(infoHash);

        if (event == RequestEvent.STOPPED) {
            this.peerIdPerTorrent.remove(infoHash);
        }

        return peerId;
    }
}
