package com.example.appfe;
import com.example.appfe.Interface.personaInterface;
import com.example.appfe.Models.personaModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PersonaAdapter extends ArrayAdapter<personaModel> {

    private Context mContext;
    private List<personaModel> personaList;

    public PersonaAdapter(Context context, List<personaModel> list) {
        super(context, 0, list);
        mContext = context;
        personaList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflar el diseño personalizado si no existe
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_persona, parent, false);
        }

        // Obtener la persona actual
        personaModel currentPersona = personaList.get(position);

        // Obtener los TextViews del layout de fila y asignarles los valores
        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvApellido = convertView.findViewById(R.id.tvApellido);
        TextView tvUsuario = convertView.findViewById(R.id.tvUsuario);

        tvNombre.setText(currentPersona.getNombre());
        tvApellido.setText(currentPersona.getApellido());
        tvUsuario.setText(String.valueOf(currentPersona.getId_usuario())); // Ajusta esto según el valor que quieras mostrar

        return convertView;
    }
}
