import api from "./api";

export const completeUser = async (data) => {
    const response = await api.post("/user", data);
    return response.data;
};

export const updateUser = async (data) => {
    const response = await api.put("/user", data);
    return response.data;
};

export const deleteUser = async () => {
    const response = await api.delete("/user");
    return response.data;
};
