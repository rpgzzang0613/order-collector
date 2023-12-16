"use strict";

document.addEventListener("DOMContentLoaded", () => {
    const loginBtn = document.getElementById("btn_login");

    loginBtn.addEventListener("click", async () => {
        const id = document.getElementById("user_id").value;
        const pw = document.getElementById("user_pw").value;

        const result = await login(id, pw);
        if(result.message !== "SUCCESS") {
            alert("다시 시도해주세요");
            return;
        }

        const data = result.data;
        if(!data.login_res) {
            alert("계정을 확인해주세요");
            return;
        }

        location.href = "/users/dashboard";
    });

    const login = async (id, pw) => {
        try {
            const response = await fetch("/users/login", {
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