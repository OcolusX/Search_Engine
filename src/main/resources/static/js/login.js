document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const loginButton = loginForm.querySelector('.form-btn');

    loginButton.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent default form submission

        // Получаем значения из полей ввода
        const username = loginForm.querySelector('input[name="username"]').value;
        const password = loginForm.querySelector('input[name="password"]').value;

        // Создание объекта SignInRequest
        const signInRequest = {
            username: username,
            password: password
        };

        // Опции запроса для аутентификации
        const authRequestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(signInRequest)
        };

        // URL эндпоинта для аутентификации
        const authUrl = '/auth/sign-in';

        // Отправка запроса на аутентификацию
        fetch(authUrl, authRequestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                window.location.href = '/user/';
            });
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('signUpForm');
    const loginButton = loginForm.querySelector('.form-btn');

    loginButton.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent default form submission

        // Получаем значения из полей ввода
        const username = loginForm.querySelector('input[name="username"]').value;
        const password = loginForm.querySelector('input[name="password"]').value;
        const email = loginForm.querySelector('input[name="email"]').value;

        // Создание объекта SignInRequest
        const signUpRequest = {
            username: username,
            password: password,
            email: email
        };

        // Опции запроса для аутентификации
        const authRequestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(signUpRequest)
        };

        // URL эндпоинта для аутентификации
        const authUrl = '/auth/sign-up';

        // Отправка запроса на аутентификацию
        fetch(authUrl, authRequestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                window.location.href = '/user/';
            });
    });
});
