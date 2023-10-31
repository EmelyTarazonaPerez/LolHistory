package LolHistory.bussine.externalServices;

import LolHistory.bussine.externalServices.model.Match;
import LolHistory.bussine.externalServices.model.Participant;
import LolHistory.bussine.externalServices.model.SummaryDamage;
import LolHistory.bussine.service.impl.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ConsumerGameHistory extends ConsumerRiot {
    @Autowired
    ConsumerUser consumerUserService;
    @Autowired
    DataService dataService;
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
        for(String string : list) {
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

    public Match getGameByTamp(long StartTimestamp){
        List<Match> listGames = loopResponse();
        Match checkGame = null;
        for(Match current: listGames) {
            long Timestamp = current.getInfo().getGameStartTimestamp();
            if(Timestamp == StartTimestamp){
                checkGame = current;
                break;
            }
        }
        return checkGame;
    }

    public List<SummaryDamage> getStacsPlayers(long StartTimestamp){
        List<Participant> participants = getGameByTamp(StartTimestamp).getInfo().getParticipants();
        List<SummaryDamage> listSummary = new ArrayList<>();
        for(Participant current:  participants) {
            SummaryDamage summaryDamage = new SummaryDamage();
            summaryDamage.setChampionPng("ihttp://ddragon.leagueoflegends.com/cdn/13.21.1/img/champion/"+current.getChampionName()+".png");
            summaryDamage.setDamage(current.getTotalDamageDealtToChampions());
            listSummary.add(summaryDamage);
        }
        return listSummary;
    }

}
