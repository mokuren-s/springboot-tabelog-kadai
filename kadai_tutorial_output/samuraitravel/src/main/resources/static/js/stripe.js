const stripe = Stripe('pk_test_51Qps2DJyKx1lCLAzB8Ivm25QJb2HBo9zWSXlZ3kFF4xQVKT1Svv8BeIvzT71svNdifLGJs1qjVSYhCYhQ2JXrzEL00WpZObdJN');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});