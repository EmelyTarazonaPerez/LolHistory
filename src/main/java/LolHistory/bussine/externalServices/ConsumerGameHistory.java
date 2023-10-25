package LolHistory.bussine.externalServices;

import LolHistory.bussine.externalServices.model.InfoByParticipant;
import LolHistory.bussine.externalServices.model.Match;
import LolHistory.bussine.externalServices.model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConsumerGameHistory extends ConsumerRiotService  {
    @Autowired
    ConsumerUserService consumerUserService;
    private String[] getMatchesByPlayer(){
        ResponseEntity<String[]> response = super.sendRiotRequest(
                "https://americas.api.riotgames.com/lol/match/v5/matches/by-puuid/"+ consumerUserService.getPUUID() +"/ids?start=0&count=5",
                HttpMethod.GET,
                String[].class);
        return response.getBody();
    }

    private List<Match> loopResponse ( ) {
        String[] list = getMatchesByPlayer();
        List<Match> returnList = new ArrayList<>();
        for (String string : list) {
            ResponseEntity<Match> response = super.sendRiotRequest(
                    "https://americas.api.riotgames.com/lol/match/v5/matches/" + string,
                    HttpMethod.GET,
                    Match.class);
            returnList.add(response.getBody());
        }
        return returnList;
    }

    public List<Match> getAllStats(){;
        return loopResponse();
    }

   /* public List<Participant> getListStatsByPlayer(){
        List<Match> dataHistoryGame = getAllStats();

        assert dataHistoryGame != null;

        return dataHistoryGame.stream().map(this::statsPlayer).collect(Collectors.toList());
    }

    private Participant statsPlayer (Match n) {
        List<Participant> participants = n.getInfo().getParticipants();
        Participant participant = null;

        for (Participant current : participants) {
            if (current.getPuuid().equals(consumerUserService.getPUUID())) {
                participant = current;
                participant.setDate(timeStampToDate(n));
                break;
            }
        }
        return participant;
    }*/


}