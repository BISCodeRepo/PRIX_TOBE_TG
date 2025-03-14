package com.prix;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {
//    // 환경변수
//    @Value("${welcome.message}")
//    private String message;
//
//    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
//
//    @GetMapping("/")
//    public String main(Model model) {
//        model.addAttribute("message", message);
//        model.addAttribute("tasks", tasks);
//
//        // welcome.html에 데이터 바인딩
//        return "welcome";
//    }
//
//    // /hello?name=kotlin queryString 예시
//    @GetMapping("/hello")
//    public String mainWithParam(
//            @RequestParam(name = "name", required = false, defaultValue = "")
//            String name, Model model) {
//
//        model.addAttribute("message", name);
//
//        // welcome.html에 데이터 바인딩
//        return "welcome";
//    }
}