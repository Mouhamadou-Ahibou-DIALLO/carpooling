import api from "./api";

export const login = async (email, password) => {
    const response = await api.post("/auth/login", { email, password });

    const user = response.data;

    localStorage.setItem("token", user.token);
    localStorage.setItem("refreshToken", user.refreshToken);

    return user;
};

export const register = async (username, email, password, phoneNumber) => {
    const response = await api.post("/auth/register", {
        username,
        email,
        password,
        phoneNumber
    });

    const user = response.data;

    localStorage.setItem("token", user.token);
    localStorage.setItem("refreshToken", user.refreshToken);

    return user;
};

export const getCurrentUser = async () => {
    const token = localStorage.getItem("token");

    if (!token) return null;

    const response = await api.get("/auth/me", {
        headers: { Authorization: `Bearer ${token}` }
    });

    return response.data;
};

export const logout = async () => {
    const token = localStorage.getItem("token");
    if (token) {
        await api.post("/auth/logout", null, {
            headers: { Authorization: `Bearer ${token}` }
        });
    }
    localStorage.clear();
};
