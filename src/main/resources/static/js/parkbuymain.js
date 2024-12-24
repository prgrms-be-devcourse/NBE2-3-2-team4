document.addEventListener('DOMContentLoaded', () => {
    const userName = '홍길동'; // 유저 이름 설정
    const userNameElement = document.querySelector('.user-name');
    userNameElement.textContent = userName;

    const buyTicketButton = document.querySelector('.buy-ticket-button');
    buyTicketButton.addEventListener('click', () => {
        alert('주차권 구매 페이지로 이동합니다.');
    });
});