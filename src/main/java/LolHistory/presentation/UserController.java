package LolHistory.presentation;
import LolHistory.bussine.externalServices.model.LeagueEntry;
import LolHistory.bussine.externalServices.model.PlayerAccount;
import LolHistory.bussine.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("account/{gameName}")
    public ResponseEntity<PlayerAccount> getAccountPlayer (@PathVariable  String gameName){
        return new ResponseEntity<PlayerAccount>( userService.getSummoner(gameName), HttpStatus.OK);
    }
    @GetMapping("account/league/{gameName}")
    public ResponseEntity<LeagueEntry[]> getSummaryLeague (@PathVariable  String gameName){
        return new ResponseEntity<>( userService.getSummonerLeague(gameName), HttpStatus.OK);
    }

}
