package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tw.edu.fju.miniclinic.model.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // 1. 從 Session 取得登入者的 ID
        String doctorId = (String) session.getAttribute("loggedInDoctorId");

        // 2. 檢查是否已登入，若未登入則導回登入頁
        if (doctorId == null) {
            return "redirect:/login";
        }

        // 3. 查詢醫師詳細資料
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // 4. 準備該醫師的所有掛號資料 (不限今日)
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepo.findByDoctor(doctor);

        model.addAttribute("doctor", doctor);
        model.addAttribute("today", today);
        model.addAttribute("appointments", appointments);

        return "dashboard";
    }
}