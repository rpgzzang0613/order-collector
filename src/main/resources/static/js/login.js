// login/login.js

document.addEventListener()


function attemptLogin() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
  
    // Simple validation
    if (username === 'your_username' && password === 'your_password') 
    {
      alert('Login successful!');
      redirectToDashboard();
      // Redirect to another page or perform additional actions
    } else {
      alert('Invalid username or password. Please try again.');
    }
  }

  // 대시보드 페이지로 이동
function redirectToDashboard() {
    alert(' dashboard.');
    /*dashboard 로 이동 */
    window.location.href = "../dashboard/dashboard.html";
  }



  document.addEventListener( "DOMContentLoaded", () =>{

     const loginBtn = document.getElementById("btn_login") ;
     loginBtn.addEventListener("click", async() =>{
      const id = document.getElementById("user_id");
      const pw = document.getElementById("user_pw");

      await login(id,pw);
     });

      const login = async (id,pw)=> {
          try {
                  const response = await fetch("/users/login" , {
                      cache:"no-cache",
                      method:"POST",
                      headers: {
                          "Content-Type": "application/json"
                      },
                      body: JSON.stringify({
                          id :id,
                          pw: pw
                      })
                  });

                  return response.json();

          }catch(e){
              console.error(e);
          }
      };
  });