package LolHistory.bussine.service.impl;

import LolHistory.bussine.externalServices.ConsumerGameHistory;
import LolHistory.bussine.externalServices.ConsumerUser;
import LolHistory.bussine.externalServices.model.Match;
import LolHistory.bussine.externalServices.model.Participant;
import LolHistory.bussine.externalServices.model.SummaryDamage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameHistoryService {
    @Autowired
    private ConsumerGameHistory consumerGameHistory;
    @Autowired
    private ConsumerUser consumerUserService;
    @Autowired
    private DataService dataService;

    public List<Match> getAllGameStatistics (){
        return consumerGameHistory.getAllStats();
    }

    public Match getGameByTamp (long StartTimestamp){
        return consumerGameHistory.getGameByTamp(StartTimestamp);
    }

    public List<SummaryDamage> getSummaryPlayers (long StartTimestamp) {
        return consumerGameHistory.getStacsPlayers(StartTimestamp);
    }

    public List<Participant> getListStatsByPlayer(){
        List<Match> dataHistoryGame = consumerGameHistory.getAllStats();
        assert dataHistoryGame != null;
        return dataHistoryGame.stream().map(this::statsPlayer).collect(Collectors.toList());
    }

    private Participant statsPlayer (Match n) {
        List<Participant> participants = n.getInfo().getParticipants();
        Participant participant = null;

        for (Participant current : participants) {
            if (current.getPuuid().equals(consumerUserService.getPUUID())) {
                participant = current;
                participant.setDate(dataService.timeStampToDate(n));
                participant.setGameMode(n.getInfo().getGameMode());
                break;
            }
        }
        return participant;
    }



}
