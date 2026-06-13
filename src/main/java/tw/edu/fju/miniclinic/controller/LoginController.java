package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import tw.edu.fju.miniclinic.model.*;

@Controller
public class LoginController {

    @Autowired
    private DoctorRepository doctorRepo;

    // GET：顯示登入頁
    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    // POST：處理登入
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        // 步驟 1：檢查表單驗證
        if (result.hasErrors()) {
            return "login";  // 顯示錯誤訊息
        }

        // 步驟 2：查詢醫師
        Doctor doctor = doctorRepo.findById(form.getDoctorId()).orElse(null);
        System.out.println("DEBUG: 登入嘗試 - ID: " + form.getDoctorId());

        // 步驟 3：檢查密碼
        // 增加 null 檢查，避免當資料庫中的 password_hash 為空時拋出 "salt cannot be null" 異常
        String storedHash = (doctor != null) ? doctor.getPasswordHash() : null;
        System.out.println("DEBUG: 資料庫中的雜湊值: " + storedHash);

        if (doctor == null) {
            model.addAttribute("errorMessage", "除錯訊息：找不到此醫師編號");
            return "login";
        }
        
        if (storedHash == null || !BCrypt.checkpw(form.getPassword(), storedHash)) {
            System.out.println("DEBUG: 密碼比對失敗");
            model.addAttribute("errorMessage", "除錯訊息：密碼錯誤或資料庫內無密碼雜湊");
            return "login";
        }

        // 步驟 4：登入成功，存入 Session
        session.setAttribute("loggedInDoctorId", doctor.getDoctorId());
        session.setAttribute("loggedInDoctorName", doctor.getName());

        return "redirect:/dashboard";
    }

    // 登出
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 清除 Session
        return "redirect:/login";
    }
}
