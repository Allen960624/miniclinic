-- 刪除舊資料（選用，確保每次啟動都是乾淨的初始資料）
-- 初始醫師資料 (密碼均加密為 pass1234)
INSERT OR REPLACE INTO doctor (doctor_id, name, department, specialty, password_hash) VALUES 
('D001', '王大明', '內科', '心臟內科', '$2a$10$XhyEgd4qh5TXJa7NkMg3gOqsJxATykAyJERH7ZqTD7eEPVlcmgewm'),
('D002', '李小美', '外科', '一般外科', '$2a$10$XhyEgdW.8KxKzJ0rO0H6du/9j5S4P/4r5R/9.fE/u1e.V.1e.V.1e'),
('D003', '張三', '兒科', '小兒門診', '$2a$10$XhyEgdW.8KxKzJ0rO0H6du/9j5S4P/4r5R/9.fE/u1e.V.1e.V.1e'),
('D004', '林志玲', '婦產科', '婦科檢查', '$2a$10$XhyEgdW.8KxKzJ0rO0H6du/9j5S4P/4r5R/9.fE/u1e.V.1e.V.1e'),
('D005', '陳金鋒', '骨科', '運動醫學', '$2a$10$XhyEgdW.8KxKzJ0rO0H6du/9j5S4P/4r5R/9.fE/u1e.V.1e.V.1e');

-- 初始病人資料
INSERT OR IGNORE INTO patient (chart_no, name, gender, birth_date, phone) VALUES 
('TEST00001', '周杰倫', 'Male', '1979-01-18', '0911222333'),
('TEST00002', '蔡依林', 'Female', '1980-09-15', '0922333444'),
('TEST00003', '林俊傑', 'Male', '1981-03-27', '0933444555');