module.exports = {
    "env": {
        "browser": true,
        "es2021": true
    },
    "extends": [
        "eslint:recommended",
        "plugin:vue/vue3-essential"
    ],
    "parserOptions": {
        "ecmaVersion": 12,
        "sourceType": "module"
    },
    "plugins": [
        "vue"
    ],
    "rules": {
        'vue/no-undef': 'off', // Composition API 관련 no-undef 끄기
        'no-undef': 'off' // defineEmits와 같은 전역 인식
    }
};
