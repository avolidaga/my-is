package ru.se.ifmo.tinder.controllers;

        import io.swagger.v3.oas.annotations.security.SecurityRequirement;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;
        import ru.se.ifmo.tinder.dto.ShootingDto;
        import ru.se.ifmo.tinder.dto.UserDataDto;
        import ru.se.ifmo.tinder.model.UserData;
        import ru.se.ifmo.tinder.service.ShootingService;
        import ru.se.ifmo.tinder.service.UserDataService;

        import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/form")
//@SecurityRequirement(name = "basicAuth")
public class FormController {

    private final UserDataService userDataService;


    @PostMapping("submitForm")
    public ResponseEntity<Integer> submitForm(@RequestBody UserDataDto userDataDto){
        Integer id = userDataService.insertUserData(userDataDto.getBirth_date(),userDataDto.getSex(),userDataDto.getWeight(),userDataDto.getHeight(),userDataDto.getHair_color(),userDataDto.getLocation());
        return ResponseEntity.ok(id);
    }


}