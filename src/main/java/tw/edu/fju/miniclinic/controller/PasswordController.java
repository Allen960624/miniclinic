package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PasswordForm;

@Controller
public class PasswordController {

    @Autowired
    private DoctorRepository doctorRepo;

    @GetMapping("/password")
    public String showPasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "password";
    }

    @PostMapping("/password")
    public String changePassword(
            @Valid @ModelAttribute("passwordForm") PasswordForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        if (result.hasErrors()) {
            return "password";
        }

        // 1. 檢查新密碼是否一致
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "兩次輸入的新密碼不一致");
            return "password";
        }

        // 2. 取得目前醫師
        String doctorId = (String) session.getAttribute("loggedInDoctorId");
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

        if (doctor == null) {
            return "redirect:/login";
        }

        // 3. 驗證舊密碼
        if (!BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            result.rejectValue("oldPassword", "error.oldPassword", "舊密碼錯誤");
            return "password";
        }

        // 4. 更新密碼（雜湊處理）
        String hashed = BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt());
        doctor.setPasswordHash(hashed);
        doctorRepo.save(doctor);

        model.addAttribute("successMessage", "密碼修改成功！");
        // 清空表單
        model.addAttribute("passwordForm", new PasswordForm());
        return "password";
    }
}