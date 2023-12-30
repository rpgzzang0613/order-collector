"use strict";

document.addEventListener("DOMContentLoaded", () => {
    const loginBtn = document.getElementById("btn_login");

    loginBtn.addEventListener("click", async () => {
        const id = document.getElementById("user_id").value;
        const pw = document.getElementById("user_pw").value;

        const result = await login(id, pw);
        if(result.message === "DATA_NOT_FOUND") {
            alert("계정 정보를 확인해주세요");
            return;
        }

        if(result.message !== "SUCCESS") {
            alert("관리자에게 문의해주세요");
            return;
        }

        location.href = "/dashboard";
    });

    const login = async (id, pw) => {
        try {
            const response = await fetch("/accounts/login", {
                cache: "no-cache",
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    user_id: id,
                    user_pw: pw
                })
            });

            return response.json();

        } catch (e) {
            console.error(e);
        }
    };
});